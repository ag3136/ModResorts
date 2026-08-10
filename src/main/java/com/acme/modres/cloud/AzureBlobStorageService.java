package com.acme.modres.cloud;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;

/**
 * Azure Blob Storage adapter used to externalize persistent application data from
 * the local container file system. The service authenticates with Managed
 * Identity through DefaultAzureCredential when a connection string is not
 * supplied.
 */
public final class AzureBlobStorageService {
  private static final Logger LOGGER = Logger.getLogger(AzureBlobStorageService.class.getName());

  private static final String CONNECTION_STRING_ENV = "AZURE_STORAGE_CONNECTION_STRING";
  private static final String ACCOUNT_ENDPOINT_ENV = "AZURE_STORAGE_BLOB_ENDPOINT";
  private static final String CONTAINER_ENV = "AZURE_STORAGE_CONTAINER";
  private static final String DEFAULT_CONTAINER = "modresorts";

  private static final AzureBlobStorageService INSTANCE = new AzureBlobStorageService();

  private final BlobContainerClient containerClient;

  private AzureBlobStorageService() {
    this.containerClient = buildContainerClient();
  }

  public static AzureBlobStorageService getInstance() {
    return INSTANCE;
  }

  public InputStream openInputStream(String blobName) throws IOException {
    BlobClient blobClient = containerClient.getBlobClient(blobName);
    if (!blobClient.exists()) {
      throw new IOException("Azure Blob not found: " + blobName);
    }
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    blobClient.downloadStream(outputStream);
    return new ByteArrayInputStream(outputStream.toByteArray());
  }

  public byte[] readAllBytes(String blobName) throws IOException {
    try (InputStream inputStream = openInputStream(blobName);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
      byte[] buffer = new byte[8192];
      int bytesRead;
      while ((bytesRead = inputStream.read(buffer)) != -1) {
        outputStream.write(buffer, 0, bytesRead);
      }
      return outputStream.toByteArray();
    }
  }

  public void uploadBytes(String blobName, byte[] content, String contentType) {
    BlobClient blobClient = containerClient.getBlobClient(blobName);
    blobClient.upload(new ByteArrayInputStream(content), content.length, true);
    if (contentType != null && contentType.trim().length() > 0) {
      blobClient.setHttpHeaders(new com.azure.storage.blob.models.BlobHttpHeaders().setContentType(contentType));
    }
  }

  public String uploadText(String blobName, String content) {
    byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
    uploadBytes(blobName, bytes, "text/plain; charset=utf-8");
    return blobName;
  }

  private BlobContainerClient buildContainerClient() {
    String containerName = getEnv(CONTAINER_ENV, DEFAULT_CONTAINER);
    String connectionString = System.getenv(CONNECTION_STRING_ENV);
    BlobContainerClientBuilder builder = new BlobContainerClientBuilder().containerName(containerName);

    if (connectionString != null && connectionString.trim().length() > 0) {
      builder.connectionString(connectionString);
    } else {
      String endpoint = System.getenv(ACCOUNT_ENDPOINT_ENV);
      if (endpoint == null || endpoint.trim().length() == 0) {
        throw new IllegalStateException("Azure Blob Storage endpoint must be supplied with " + ACCOUNT_ENDPOINT_ENV
            + " when " + CONNECTION_STRING_ENV + " is not configured.");
      }
      builder.endpoint(endpoint).credential(new DefaultAzureCredentialBuilder().build());
    }

    BlobContainerClient client = builder.buildClient();
    try {
      client.createIfNotExists();
    } catch (RuntimeException e) {
      LOGGER.log(Level.WARNING, "Unable to create or verify Azure Blob container; continuing with configured client", e);
    }
    return client;
  }

  private String getEnv(String name, String defaultValue) {
    String value = System.getenv(name);
    return value == null || value.trim().isEmpty() ? defaultValue : value.trim();
  }
}
