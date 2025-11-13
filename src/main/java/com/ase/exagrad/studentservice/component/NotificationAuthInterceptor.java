package com.ase.exagrad.studentservice.component;

import com.ase.exagrad.studentservice.service.external.KeycloakClient;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationAuthInterceptor implements ClientHttpRequestInterceptor {

  private final KeycloakClient keycloakClient;

  @Override
  public ClientHttpResponse intercept(
      HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
    String token = keycloakClient.getAccessToken();
    if (token != null) {
      request.getHeaders().setBearerAuth(token);
      log.debug("Added Bearer token to notification request: {}", request.getURI());
    } else {
      log.warn("No access token available for notification request: {}", request.getURI());
    }
    return execution.execute(request, body);
  }
}
