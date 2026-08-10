package com.acme.modres.mbean;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.acme.modres.mbean.reservation.ReservationList;
import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.google.gson.Gson;

public final class IOUtils {

  private static final String AZURE_STORAGE_CONNECTION_STRING = "AZURE_STORAGE_CONNECTION_STRING";
  private static final String AZURE_STORAGE_CONTAINER_NAME = "AZURE_STORAGE_CONTAINER_NAME";
  private static final String AZURE_SERVICE_BUS_CONNECTION_STRING = "AZURE_SERVICE_BUS_CONNECTION_STRING";
  private static final String AZURE_SERVICE_BUS_QUEUE_NAME = "AZURE_SERVICE_BUS_QUEUE_NAME";
  private static final String DEFAULT_CONTAINER_NAME = "modresorts";

  private IOUtils() {
  }

  private static BlobContainerClient getBlobContainerClient() {
    String connectionString = System.getenv(AZURE_STORAGE_CONNECTION_STRING);
    if (connectionString == null || connectionString.trim().isEmpty()) {
      return null;
    }

    String containerName = System.getenv(AZURE_STORAGE_CONTAINER_NAME);
    if (containerName == null || containerName.trim().isEmpty()) {
      containerName = DEFAULT_CONTAINER_NAME;
    }

    return new BlobContainerClientBuilder()
        .connectionString(connectionString)
        .containerName(containerName)
        .buildClient();
  }

  public static byte[] getBytesFromAzureBlobOrClasspath(String path) throws IOException {
    BlobContainerClient containerClient = getBlobContainerClient();
    if (containerClient != null) {
      BlobClient blobClient = containerClient.getBlobClient(path);
      if (blobClient.exists()) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
          blobClient.downloadStream(outputStream);
          return outputStream.toByteArray();
        }
      }
    }

    try (InputStream initialStream = IOUtils.class.getClassLoader().getResourceAsStream(path)) {
      if (initialStream == null) {
        throw new IOException("Unable to locate resource in Azure Blob Storage or classpath: " + path);
      }
      try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
        byte[] buffer = new byte[4096];
        int length;
        while ((length = initialStream.read(buffer)) != -1) {
          outputStream.write(buffer, 0, length);
        }
        return outputStream.toByteArray();
      }
    }
  }

  public static void uploadBytesToAzureBlob(String path, byte[] content, String contentType) throws IOException {
    BlobContainerClient containerClient = getBlobContainerClient();
    if (containerClient == null) {
      throw new IOException("Azure Blob Storage is not configured. Set " + AZURE_STORAGE_CONNECTION_STRING
          + " and optionally " + AZURE_STORAGE_CONTAINER_NAME + ".");
    }

    BlobClient blobClient = containerClient.getBlobClient(path);
    try (ByteArrayInputStream inputStream = new ByteArrayInputStream(content)) {
      blobClient.upload(inputStream, content.length, true);
    }
    if (contentType != null && !contentType.trim().isEmpty()) {
      blobClient.setHttpHeaders(new com.azure.storage.blob.models.BlobHttpHeaders().setContentType(contentType));
    }
  }

  public static byte[] createZipEntry(String entryName, byte[] content) throws IOException {
    try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {
      ZipEntry zipEntry = new ZipEntry(entryName);
      zipOutputStream.putNextEntry(zipEntry);
      zipOutputStream.write(content);
      zipOutputStream.closeEntry();
      zipOutputStream.finish();
      return byteArrayOutputStream.toByteArray();
    }
  }

  public static boolean isZipValid(byte[] zipContent) throws IOException {
    try (ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(zipContent))) {
      return zipInputStream.getNextEntry() != null;
    }
  }

  public static void scheduleAvailabilityCheck(String selectedDateStr) {
    if (selectedDateStr == null || selectedDateStr.trim().isEmpty()) {
      return;
    }
    String connectionString = System.getenv(AZURE_SERVICE_BUS_CONNECTION_STRING);
    String queueName = System.getenv(AZURE_SERVICE_BUS_QUEUE_NAME);
    if (connectionString == null || connectionString.trim().isEmpty() || queueName == null || queueName.trim().isEmpty()) {
      return;
    }

    ServiceBusMessage message = new ServiceBusMessage("{\"selectedDate\":\"" + selectedDateStr + "\"}");
    try (ServiceBusSenderClient sender = new ServiceBusClientBuilder()
        .connectionString(connectionString)
        .sender()
        .queueName(queueName)
        .buildClient()) {
      sender.scheduleMessage(message, OffsetDateTime.now(java.time.ZoneOffset.UTC).plus(Duration.ofMinutes(1)));
    }
  }

  public static OpMetadataList getOpListFromConfig() {
    try (Reader reader = new InputStreamReader(new ByteArrayInputStream(getBytesFromAzureBlobOrClasspath("ops.json")),
        StandardCharsets.UTF_8)) {
      return new Gson().fromJson(reader, OpMetadataList.class);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static ReservationList getReservationListFromConfig() {
    try (Reader reader = new InputStreamReader(new ByteArrayInputStream(getBytesFromAzureBlobOrClasspath("reservations.json")),
        StandardCharsets.UTF_8)) {
      return new Gson().fromJson(reader, ReservationList.class);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

}
