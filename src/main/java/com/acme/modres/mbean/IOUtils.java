package com.acme.modres.mbean;

import java.io.IOException;
import java.io.InputStream;

import com.acme.modres.mbean.reservation.ReservationList;
import com.acme.modres.util.JsonInputStream;

/**
 * Cloud-native IOUtils that reads from classpath resources instead of local file system.
 * Eliminates temporary file creation and local file system dependencies.
 */
public final class IOUtils {

  /**
   * Read resource from classpath as InputStream.
   * This eliminates the need for temporary files and local file system writes.
   * 
   * @param path Resource path in classpath
   * @return InputStream of the resource
   */
  public static InputStream getResourceAsStream(String path) {
    return IOUtils.class.getClassLoader().getResourceAsStream(path);
  }

  /**
   * Get operation metadata list from configuration.
   * Reads directly from classpath resources without creating temporary files.
   * 
   * @return OpMetadataList or null if error occurs
   */
  public static OpMetadataList getOpListFromConfig() {
    try (InputStream resourceStream = getResourceAsStream("ops.json")) {
      if (resourceStream == null) {
        System.err.println("ops.json not found in classpath");
        return null;
      }
      
      try (JsonInputStream is = new JsonInputStream(resourceStream)) {
        OpMetadataList opList = new OpMetadataList(); // empty default
        opList = (OpMetadataList) is.parseJsonAs(OpMetadataList.class);
        return opList;
      }
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Get reservation list from configuration.
   * Reads directly from classpath resources without creating temporary files.
   * 
   * @return ReservationList or null if error occurs
   */
  public static ReservationList getReservationListFromConfig() {
    try (InputStream resourceStream = getResourceAsStream("reservations.json")) {
      if (resourceStream == null) {
        System.err.println("reservations.json not found in classpath");
        return null;
      }
      
      try (JsonInputStream is = new JsonInputStream(resourceStream)) {
        ReservationList reservationList = new ReservationList(); // empty default
        reservationList = (ReservationList) is.parseJsonAs(ReservationList.class);
        return reservationList;
      }
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

}
