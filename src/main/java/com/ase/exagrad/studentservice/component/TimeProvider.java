package com.ase.exagrad.studentservice.component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TimeProvider {

  private final ZoneId appZoneId;

  public ZonedDateTime now() {
    return ZonedDateTime.now(appZoneId);
  }
}
