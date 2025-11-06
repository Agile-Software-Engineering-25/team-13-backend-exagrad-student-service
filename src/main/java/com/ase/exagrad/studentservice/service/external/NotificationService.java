package com.ase.exagrad.studentservice.service.external;

import com.ase.exagrad.studentservice.dto.external.SendEmailRequest;
import com.ase.exagrad.studentservice.dto.external.SendEmailResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final RestTemplate restTemplate;

    @Value("${app.external-apis.notification-service.base-url}") 
    private String notificationServiceUrl;

    public SendEmailResponse sendEmail(SendEmailRequest request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> payload = new HashMap<>();
            payload.put("to", request.getTo());
            payload.put("subject", request.getSubject());
            payload.put("body", request.getText());

            HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(payload, headers);

            ResponseEntity<Void> response = restTemplate.postForEntity(
                    notificationServiceUrl + "/notifications/send-email",
                    httpEntity,
                    Void.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                return new SendEmailResponse(true, "Mail erfolgreich versendet.");
            } else {
                return new SendEmailResponse(false, "Fehler beim Mailversand: " + response.getStatusCode());
            }
        } catch (Exception e) {
            return new SendEmailResponse(false, "Fehler: " + e.getMessage());
        }
    }
}
