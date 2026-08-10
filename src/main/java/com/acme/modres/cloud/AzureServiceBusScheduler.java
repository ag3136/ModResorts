package com.acme.modres.cloud;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;

/**
 * Schedules distributed, timezone-agnostic work through Azure Service Bus
 * scheduled messages instead of relying on container-local timers.
 */
public final class AzureServiceBusScheduler {
  private static final Logger LOGGER = Logger.getLogger(AzureServiceBusScheduler.class.getName());
  private static final String CONNECTION_STRING_ENV = "AZURE_SERVICE_BUS_CONNECTION_STRING";
  private static final String FULLY_QUALIFIED_NAMESPACE_ENV = "AZURE_SERVICE_BUS_NAMESPACE";
  private static final String QUEUE_ENV = "AZURE_SERVICE_BUS_QUEUE";
  private static final String DEFAULT_QUEUE = "modresorts-scheduled-work";

  private final ServiceBusSenderClient senderClient;

  public AzureServiceBusScheduler() {
    this.senderClient = buildSenderClient();
  }

  public Long scheduleMessage(String messageBody, OffsetDateTime scheduledTimeUtc) {
    ServiceBusMessage message = new ServiceBusMessage(messageBody);
    OffsetDateTime utcTime = scheduledTimeUtc.withOffsetSameInstant(ZoneOffset.UTC);
    return senderClient.scheduleMessage(message, utcTime);
  }

  public void close() {
    if (senderClient != null) {
      senderClient.close();
    }
  }

  public static void scheduleIfConfigured(String messageBody, OffsetDateTime scheduledTimeUtc) {
    if (!isConfigured()) {
      LOGGER.fine("Azure Service Bus scheduling skipped because Service Bus configuration is not present.");
      return;
    }
    AzureServiceBusScheduler scheduler = null;
    try {
      scheduler = new AzureServiceBusScheduler();
      scheduler.scheduleMessage(messageBody, scheduledTimeUtc);
    } catch (RuntimeException e) {
      LOGGER.log(Level.WARNING, "Unable to schedule Azure Service Bus message", e);
    } finally {
      if (scheduler != null) {
        scheduler.close();
      }
    }
  }

  private ServiceBusSenderClient buildSenderClient() {
    String queueName = getEnv(QUEUE_ENV, DEFAULT_QUEUE);
    String connectionString = System.getenv(CONNECTION_STRING_ENV);
    ServiceBusClientBuilder builder = new ServiceBusClientBuilder();

    if (connectionString != null && connectionString.trim().length() > 0) {
      builder.connectionString(connectionString.trim());
    } else {
      String namespace = System.getenv(FULLY_QUALIFIED_NAMESPACE_ENV);
      if (namespace == null || namespace.trim().isEmpty()) {
        throw new IllegalStateException("Azure Service Bus namespace must be supplied with "
            + FULLY_QUALIFIED_NAMESPACE_ENV + " when " + CONNECTION_STRING_ENV + " is not configured.");
      }
      builder.fullyQualifiedNamespace(namespace.trim())
          .credential(new DefaultAzureCredentialBuilder().build());
    }
    return builder.sender().queueName(queueName).buildClient();
  }

  private static boolean isConfigured() {
    return hasText(System.getenv(CONNECTION_STRING_ENV)) || hasText(System.getenv(FULLY_QUALIFIED_NAMESPACE_ENV));
  }

  private static boolean hasText(String value) {
    return value != null && value.trim().length() > 0;
  }

  private String getEnv(String name, String defaultValue) {
    String value = System.getenv(name);
    return value == null || value.trim().isEmpty() ? defaultValue : value.trim();
  }
}
