package com.ase.exagrad.studentservice.component;

import java.time.Duration;
import java.time.Instant;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

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
      HttpServletRequest request,
      HttpServletResponse response,
      Object handler,
      Exception ex) {
    Instant startTime = (Instant) request.getAttribute(START_TIME_ATTRIBUTE);
    if (startTime!=null) {
      Duration duration = Duration.between(startTime, Instant.now());

      String logLevel = getLogLevel(response.getStatus());

      if (logLevel.equals("ERROR")) {
        log.error(
            "Response: {} {} | Status: {} | Duration: {}ms",
            request.getMethod(),
            request.getRequestURI(),
            response.getStatus(),
            duration.toMillis());
      }
      else if (logLevel.equals("WARN")) {
        log.warn(
            "Response: {} {} | Status: {} | Duration: {}ms",
            request.getMethod(),
            request.getRequestURI(),
            response.getStatus(),
            duration.toMillis());
      }
      else {
        log.info(
            "Response: {} {} | Status: {} | Duration: {}ms",
            request.getMethod(),
            request.getRequestURI(),
            response.getStatus(),
            duration.toMillis());
      }

      if (ex!=null) {
        log.error("Exception during request: {}", ex.getMessage(), ex);
      }
    }
  }

  private String getClientIp(HttpServletRequest request) {
    String xForwardedFor = request.getHeader("X-Forwarded-For");
    if (xForwardedFor!=null && !xForwardedFor.isEmpty()) {
      return xForwardedFor.split(",")[0].trim();
    }
    return request.getRemoteAddr();
  }

  private String getLogLevel(int statusCode) {
    if (statusCode >= 500) {
      return "ERROR";
    }
    else if (statusCode >= 400) {
      return "WARN";
    }
    return "INFO";
  }
}
