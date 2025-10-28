package com.ase.exagrad.studentservice.component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class RequestLoggingInterceptor implements HandlerInterceptor {

  private static final String START_TIME_ATTRIBUTE = "startTime";

  @Override
  public boolean preHandle(
      HttpServletRequest request, HttpServletResponse response, Object handler) {
    Instant startTime = Instant.now();
    request.setAttribute(START_TIME_ATTRIBUTE, startTime);

    log.info(
        "Incoming request: {} {} | Client: {} | User-Agent: {}",
        request.getMethod(),
        request.getRequestURI(),
        getClientIp(request),
        request.getHeader("User-Agent"));

    return true;
  }

  @Override
  public void afterCompletion(
      HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    Instant startTime = (Instant) request.getAttribute(START_TIME_ATTRIBUTE);
    if (startTime != null) {
      Duration duration = Duration.between(startTime, Instant.now());

      Level level = getLogLevel(response.getStatus());
      logAtLevel(
          level,
          "Response: {} {} | Status: {} | Duration: {}ms",
          request.getMethod(),
          request.getRequestURI(),
          response.getStatus(),
          duration.toMillis());

      if (ex != null) {
        log.error("Exception during request: {}", ex.getMessage(), ex);
      }
    }
  }

  private String getClientIp(HttpServletRequest request) {
    String xForwardedFor = request.getHeader("X-Forwarded-For");
    if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
      return xForwardedFor.split(",")[0].trim();
    }
    return request.getRemoteAddr();
  }

  private Level getLogLevel(int statusCode) {
    if (statusCode >= HttpStatus.INTERNAL_SERVER_ERROR.value()) {
      return Level.ERROR;
    } else if (statusCode >= HttpStatus.BAD_REQUEST.value()) {
      return Level.WARN;
    }
    return Level.INFO;
  }

  private void logAtLevel(Level level, String message, Object... args) {
    switch (level) {
      case ERROR -> log.error(message, args);
      case WARN -> log.warn(message, args);
      case INFO -> log.info(message, args);
      case DEBUG -> log.debug(message, args);
      case TRACE -> log.trace(message, args);
    }
  }
}
