package com.ase.exagrad.studentservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "app.file")
public class FileProperties {
  private long maxSize; // in bytes
  private String allowedTypes;
}
