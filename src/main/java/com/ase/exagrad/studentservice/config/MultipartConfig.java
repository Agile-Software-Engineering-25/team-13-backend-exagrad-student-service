package com.ase.exagrad.studentservice.config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import jakarta.servlet.MultipartConfigElement;

@Configuration
public class MultipartConfig {

  private final FileProperties fileProperties;

  public MultipartConfig(FileProperties fileProperties) {
    this.fileProperties = fileProperties;
  }

  @Bean
  public MultipartConfigElement multipartConfigElement() {
    MultipartConfigFactory factory = new MultipartConfigFactory();
    DataSize maxSize = DataSize.ofBytes(fileProperties.getMaxSize());
    factory.setMaxFileSize(maxSize);
    factory.setMaxRequestSize(maxSize);
    return factory.createMultipartConfig();
  }
}
