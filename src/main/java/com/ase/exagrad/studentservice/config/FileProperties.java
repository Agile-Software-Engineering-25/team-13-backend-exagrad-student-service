package com.ase.exagrad.studentservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Data;

@ConfigurationProperties(prefix = "app.file")
@Component
@Data
public class FileProperties {
  private long maxSize; // in bytes
  private String allowedTypes;
}
