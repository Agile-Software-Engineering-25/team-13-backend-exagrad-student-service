package com.ase.exagrad.studentservice.service.external;

import com.ase.exagrad.studentservice.config.KeycloakConfig;
import com.ase.exagrad.studentservice.dto.keycloak.TokenResponse;
import java.time.Duration;
import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Client for interacting with the Keycloak administration API. Handles obtaining admin access
 * tokens and fetching user information.
 */
@Service
@Slf4j
public class KeycloakClient {

  private static final int DEFAULT_TOKEN_EXPIRY_SECONDS = 300;
  private static final int TOKEN_REFRESH_BUFFER_SECONDS = 30;

  private final WebClient webClient;
  private final KeycloakConfig keycloakConfig;
  private String accessToken;
  private Instant tokenExpirationTime;

  /**
   * Constructs a new KeycloakClient with the provided configuration.
   *
   * @param keycloakConfig Configuration containing Keycloak settings
   */
  public KeycloakClient(KeycloakConfig keycloakConfig) {
    this.keycloakConfig = keycloakConfig;
    this.webClient = WebClient.builder().build();
  }

  /**
   * Obtains an access token from Keycloak using client credentials. The token is cached and reused
   * until it expires.
   *
   * @return The access token string.
   */
  public String getAccessToken() {
    if (isTokenValid()) {
      return accessToken;
    }

    String tokenUrl =
        keycloakConfig.getServerUrl()
            + "/realms/"
            + keycloakConfig.getRealm()
            + "/protocol/openid-connect/token";

    MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
    formData.add("client_id", keycloakConfig.getClientId());
    formData.add("client_secret", keycloakConfig.getClientSecret());
    formData.add("grant_type", keycloakConfig.getGrantType());

    try {
      TokenResponse tokenResponse =
          webClient
              .post()
              .uri(tokenUrl)
              .contentType(MediaType.APPLICATION_FORM_URLENCODED)
              .body(BodyInserters.fromFormData(formData))
              .retrieve()
              .bodyToMono(TokenResponse.class)
              .block();

      if (tokenResponse != null && tokenResponse.getAccessToken() != null) {
        this.accessToken = tokenResponse.getAccessToken();
        int expiresIn =
            tokenResponse.getExpiresIn() != null
                ? tokenResponse.getExpiresIn()
                : DEFAULT_TOKEN_EXPIRY_SECONDS;
        this.tokenExpirationTime =
            Instant.now().plus(Duration.ofSeconds(expiresIn - TOKEN_REFRESH_BUFFER_SECONDS));
        log.info("Successfully obtained new access token.");
        return this.accessToken;
      } else {
        log.error("Failed to obtain access token: null response");
        return null;
      }
    } catch (Exception e) {
      log.error("Failed to obtain access token", e);
      return null;
    }
  }

  private boolean isTokenValid() {
    return accessToken != null
        && tokenExpirationTime != null
        && Instant.now().isBefore(tokenExpirationTime);
  }
}
