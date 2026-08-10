package com.acme.modres.mbean;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import com.acme.modres.cloud.AzureBlobStorageService;
import com.acme.modres.mbean.reservation.ReservationList;
import com.google.gson.Gson;

public final class IOUtils {

  private static final String AZURE_STORAGE_ENABLED = "AZURE_STORAGE_ENABLED";
  private static final Gson GSON = new Gson();

  private IOUtils() {
  }

  public static InputStream getInputStreamFromStorage(String path) throws IOException {
    if (isAzureStorageEnabled()) {
      return AzureBlobStorageService.getInstance().openInputStream(path);
    }

    InputStream inputStream = IOUtils.class.getClassLoader().getResourceAsStream(path);
    if (inputStream == null) {
      throw new IOException("Resource not found: " + path);
    }
    return inputStream;
  }

  public static byte[] getBytesFromStorage(String path) throws IOException {
    try (InputStream initialStream = getInputStreamFromStorage(path);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
      byte[] buffer = new byte[8192];
      int bytesRead;
      while ((bytesRead = initialStream.read(buffer)) != -1) {
        outputStream.write(buffer, 0, bytesRead);
      }
      return outputStream.toByteArray();
    }
  }

  public static String writeBytesToStorage(String path, byte[] bytes, String contentType) {
    AzureBlobStorageService.getInstance().uploadBytes(path, bytes, contentType);
    return path;
  }

  public static OpMetadataList getOpListFromConfig() {
    return parseJsonFromStorage("ops.json", OpMetadataList.class);
  }

  public static ReservationList getReservationListFromConfig() {
    return parseJsonFromStorage("reservations.json", ReservationList.class);
  }

  private static <T> T parseJsonFromStorage(String path, Class<T> cls) {
    try (InputStream inputStream = getInputStreamFromStorage(path)) {
      return GSON.fromJson(new java.io.InputStreamReader(inputStream, StandardCharsets.UTF_8), cls);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  private static boolean isAzureStorageEnabled() {
    String value = System.getenv(AZURE_STORAGE_ENABLED);
    return value == null || Boolean.parseBoolean(value);
  }
}
