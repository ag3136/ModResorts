package com.acme.modres;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.channels.Channels;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.acme.modres.mbean.IOUtils;
import com.acme.modres.mbean.reservation.DateChecker;
import com.acme.modres.mbean.reservation.ReservationCheckerData;
import com.acme.modres.mbean.reservation.Reservation;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

@WebServlet({ "/resorts/availability" })
public class AvailabilityCheckerServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  private static final Logger logger = Logger.getLogger(AvailabilityCheckerServlet.class.getName());

  private static InitialContext context;

  private ReservationCheckerData reservationCheckerData;

  // GCS bucket name read from environment variable for cloud-native configuration
  private static final String GCS_BUCKET_NAME = System.getenv("GCS_BUCKET_NAME") != null
      ? System.getenv("GCS_BUCKET_NAME")
      : "modresorts-data";

  @Override
  public void init() {
    // load reserved dates
    this.reservationCheckerData = new ReservationCheckerData(IOUtils.getReservationListFromConfig());
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

    String methodName = "doGet";
    logger.entering(AvailabilityCheckerServlet.class.getName(), methodName);
    int statusCode = 200;

    String selectedDateStr = request.getParameter("date");
    boolean parsedDate = reservationCheckerData.setSelectedDate(selectedDateStr);
    if (!parsedDate || reservationCheckerData.getReservationList() == null) {
      statusCode = 500;
      reservationCheckerData.setAvailablility(false);
    } else {
      List<Reservation> reservations = reservationCheckerData.getReservationList().getReservations();
      boolean isAvailible = true;

      for (Reservation reservation : reservations) {
        try {
          // Standardize on UTC to avoid server-local timezone dependencies (blocker-10, blocker-11)
          SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATA_FORMAT);
          sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
          Date fromDate = sdf.parse(reservation.getFromDate());
          Date toDate = sdf.parse(reservation.getToDate());
          Date selectedDate = reservationCheckerData.getSelectedDate();

          if (selectedDate.after(fromDate) && selectedDate.before(toDate)) {
            isAvailible = false;
            break;
          }
        } catch (ParseException ex) {
          ex.printStackTrace();
        }
      }

      reservationCheckerData.setAvailablility(isAvailible);

      // Adjust the status code based on availability
      if (!isAvailible) {
        statusCode = 201;
      }
    }

    // Send the response
    PrintWriter out = response.getWriter();
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    out.print("{\"availability\": \"" + String.valueOf(reservationCheckerData.isAvailible()) + "\"}");
    response.setStatus(statusCode);
  }

  /**
   * Returns the weather information for a given city
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    doGet(request, response);
  }

  /**
   * Exports reservations as a ZIP file to Google Cloud Storage instead of local filesystem.
   * Replaces hard-coded file paths (blocker-1), local file write operations (blocker-2),
   * java.io.File usage (blocker-4), and resource leaks (blocker-5) with GCS-backed operations
   * using try-with-resources for automatic resource management.
   */
  protected int exportRevervations(String selectedDateStr) {
    String objectName = "exports/reservations.zip";

    try {
      // Read reservations content from classpath resource
      byte[] reservationsContent;
      try (InputStream resourceStream = IOUtils.class.getClassLoader()
          .getResourceAsStream("reservations.json")) {
        if (resourceStream == null) {
          logger.warning("reservations.json not found in classpath");
          return -1;
        }
        reservationsContent = resourceStream.readAllBytes();
      }

      // Build ZIP content in memory and upload directly to GCS (blocker-1, blocker-2, blocker-4)
      Storage storage = StorageOptions.getDefaultInstance().getService();
      BlobId blobId = BlobId.of(GCS_BUCKET_NAME, objectName);
      BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
          .setContentType("application/zip")
          .build();

      // Use try-with-resources for automatic resource management (blocker-5)
      try (java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
           ZipOutputStream zipOut = new ZipOutputStream(baos)) {
        ZipEntry zipEntry = new ZipEntry("reservations.json");
        zipOut.putNextEntry(zipEntry);
        zipOut.write(reservationsContent);
        zipOut.closeEntry();
        zipOut.finish();

        byte[] zipBytes = baos.toByteArray();
        // Upload ZIP bytes directly to GCS — no local file system dependency
        storage.create(blobInfo, zipBytes);
        logger.info("Reservations ZIP uploaded to GCS: gs://" + GCS_BUCKET_NAME + "/" + objectName);
        return 0;
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (Throwable e) {
      e.printStackTrace();
    }
    return -1;
  }

}
