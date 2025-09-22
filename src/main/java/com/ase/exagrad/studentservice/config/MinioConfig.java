package com.ase.exagrad.studentservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class MinioConfig {

  private final MinioConnectionProperties minioConnectionProperties;

  @Bean
  public MinioClient minioClient() {
    return MinioClient.builder()
        .endpoint(minioConnectionProperties.getEndpoint())
        .credentials(
            minioConnectionProperties.getAccessKey(),
            minioConnectionProperties.getSecretKey())
        .build();
  }
}
