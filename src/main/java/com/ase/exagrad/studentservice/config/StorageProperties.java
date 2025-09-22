package com.ase.exagrad.studentservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "app.storage")
public class StorageProperties {
  private String examDocumentsBucket;
  private String pubDocumentsBucket;
}
