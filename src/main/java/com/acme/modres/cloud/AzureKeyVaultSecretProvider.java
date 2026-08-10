package com.acme.modres.cloud;

import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;

/**
 * Retrieves application secrets from Azure Key Vault using Managed Identity via
 * DefaultAzureCredential. Secret names and vault URL are supplied through
 * environment variables so no credentials are embedded in source code.
 */
public final class AzureKeyVaultSecretProvider {
  private static final String KEY_VAULT_URL_ENV = "AZURE_KEY_VAULT_URL";

  private final SecretClient secretClient;

  public AzureKeyVaultSecretProvider() {
    String vaultUrl = System.getenv(KEY_VAULT_URL_ENV);
    if (vaultUrl == null || vaultUrl.trim().isEmpty()) {
      throw new IllegalStateException(KEY_VAULT_URL_ENV + " must be configured to read secrets from Azure Key Vault.");
    }
    this.secretClient = new SecretClientBuilder()
        .vaultUrl(vaultUrl.trim())
        .credential(new DefaultAzureCredentialBuilder().build())
        .buildClient();
  }

  public String getSecret(String secretName) {
    if (secretName == null || secretName.trim().isEmpty()) {
      throw new IllegalArgumentException("Secret name must be provided.");
    }
    return secretClient.getSecret(secretName.trim()).getValue();
  }
}
