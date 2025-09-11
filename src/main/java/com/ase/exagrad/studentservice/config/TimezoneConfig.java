package com.ase.exagrad.studentservice.config;

import java.time.ZoneId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TimezoneConfig {

  @Value("${app.timezone:UTC}") // default to UTC if not set)
  private String timezone;

  @Bean
  public ZoneId appZoneId() {
    return ZoneId.of(timezone);
  }
}
