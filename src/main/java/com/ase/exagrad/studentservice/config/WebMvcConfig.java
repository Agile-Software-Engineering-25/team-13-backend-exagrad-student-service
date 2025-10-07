package com.ase.exagrad.studentservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.ase.exagrad.studentservice.component.RequestLoggingInterceptor;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

  private static final long CORS_MAX_AGE_SECONDS = 3600;
  private final RequestLoggingInterceptor requestLoggingInterceptor;
  @Value("${app.cors.allowed-origins}")
  private String[] allowedOrigins;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry
        .addInterceptor(requestLoggingInterceptor)
        .addPathPatterns("/documents/**") // Log all document endpoints
        .addPathPatterns("/api/**"); // Add other patterns as needed
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry
        .addMapping("/**")
        .allowedOrigins(allowedOrigins)
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
        .allowedHeaders("*")
        .allowCredentials(true)
        .maxAge(CORS_MAX_AGE_SECONDS);
  }
}
