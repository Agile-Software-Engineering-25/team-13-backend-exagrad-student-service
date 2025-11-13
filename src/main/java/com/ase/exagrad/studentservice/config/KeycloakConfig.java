package com.ase.exagrad.studentservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@ConfigurationProperties(prefix = "keycloak")
@Data
@Validated
public class KeycloakConfig {
  private String serverUrl;
  private String realm;
  private String clientId;
  private String clientSecret;
  private String grantType;
}
