package com.ase.exagrad.studentservice.components;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
@RequiredArgsConstructor
public class TimeProvider {

    private final ZoneId appZoneId;

    public ZonedDateTime now() {
        return ZonedDateTime.now(appZoneId);
    }
}
