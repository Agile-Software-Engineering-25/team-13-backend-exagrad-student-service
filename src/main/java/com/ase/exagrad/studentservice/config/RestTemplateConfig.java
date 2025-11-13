package com.ase.exagrad.studentservice.config;

import com.ase.exagrad.studentservice.component.KeycloakAuthInterceptor;
import com.ase.exagrad.studentservice.component.NotificationAuthInterceptor;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class RestTemplateConfig {

  private final KeycloakAuthInterceptor keycloakAuthInterceptor;
  private final NotificationAuthInterceptor notificationAuthInterceptor;

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder) {
    return builder
        .connectTimeout(Duration.ofSeconds(5))
        .readTimeout(Duration.ofSeconds(10))
        .additionalInterceptors(keycloakAuthInterceptor)
        .build();
  }

  @Bean
  public RestTemplate notificationRestTemplate(RestTemplateBuilder builder) {
    return builder
        .connectTimeout(Duration.ofSeconds(5))
        .readTimeout(Duration.ofSeconds(10))
        .additionalInterceptors(notificationAuthInterceptor)
        .build();
  }
}

