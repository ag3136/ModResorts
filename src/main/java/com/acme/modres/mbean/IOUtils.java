package com.acme.modres.mbean;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import com.acme.modres.mbean.reservation.ReservationList;
import com.acme.modres.util.JsonInputStream;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

/**
 * Utility class for I/O operations.
 *
 * Blocker-3 (cr-java-0062) and Blocker-6 (cr-java-0112):
 * Replaced local temporary file creation (File.createTempFile + FileOutputStream)
 * with Google Cloud Storage for durable, cloud-native intermediate data storage.
 * Resources are read from the classpath and, when needed externally, stored in GCS
 * rather than the ephemeral local /tmp directory.
 */
public final class IOUtils {

  // GCS bucket name read from environment variable for cloud-native configuration
  private static final String GCS_BUCKET_NAME = System.getenv("GCS_BUCKET_NAME") != null
      ? System.getenv("GCS_BUCKET_NAME")
      : "modresorts-data";

  /**
   * Reads a classpath resource and uploads it to Google Cloud Storage,
   * returning the GCS object name. Replaces File.createTempFile (blocker-6)
   * and FileOutputStream local writes (blocker-3) with GCS-backed storage.
   *
   * @param path classpath-relative resource path
   * @return GCS object name, or null on failure
   */
  public static String uploadResourceToGcs(String path) {
    InputStream initialStream = null;
    try {
      initialStream = IOUtils.class.getClassLoader().getResourceAsStream(path);
      if (initialStream == null) {
        System.err.println("Resource not found in classpath: " + path);
        return null;
      }
      byte[] buffer = initialStream.readAllBytes();

      // Upload to GCS instead of writing to local temp file (blocker-3, blocker-6)
      Storage storage = StorageOptions.getDefaultInstance().getService();
      String objectName = "temp/" + path;
      BlobId blobId = BlobId.of(GCS_BUCKET_NAME, objectName);
      BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
      storage.create(blobInfo, buffer);
      return objectName;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } finally {
      if (initialStream != null) {
        try {
          initialStream.close();
        } catch (IOException e) {
          // ignore
        }
      }
    }
  }

  /**
   * Reads a classpath resource as a byte array directly, without writing to
   * the local filesystem. Used internally for JSON parsing.
   *
   * @param path classpath-relative resource path
   * @return byte array of resource content, or null on failure
   */
  public static byte[] readResourceBytes(String path) {
    try (InputStream stream = IOUtils.class.getClassLoader().getResourceAsStream(path)) {
      if (stream == null) {
        System.err.println("Resource not found in classpath: " + path);
        return null;
      }
      return stream.readAllBytes();
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static OpMetadataList getOpListFromConfig() {
    byte[] content = readResourceBytes("ops.json");
    if (content == null) return null;
    try (JsonInputStream is = new JsonInputStream(content)) {
      OpMetadataList opList = new OpMetadataList();
      opList = (OpMetadataList) is.parseJsonAs(OpMetadataList.class);
      return opList;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static ReservationList getReservationListFromConfig() {
    byte[] content = readResourceBytes("reservations.json");
    if (content == null) return null;
    try (JsonInputStream is = new JsonInputStream(content)) {
      ReservationList reservationList = new ReservationList();
      reservationList = (ReservationList) is.parseJsonAs(ReservationList.class);
      return reservationList;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

}
