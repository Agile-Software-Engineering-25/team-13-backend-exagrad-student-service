package com.ase.exagrad.studentservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "minio-connection")
public class MinioConnectionProperties {
  private String endpoint;
  private String accessKey;
  private String secretKey;
  private String region;
}
