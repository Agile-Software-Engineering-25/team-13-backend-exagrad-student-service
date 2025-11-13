package com.ase.exagrad.studentservice.service;

import com.ase.exagrad.studentservice.dto.request.NotificationRequest;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

  private final RestTemplate notificationRestTemplate;

  @Value(
      "${notification.service.url:https://sau-portal.de/notification-service/api/v1/notifications}")
  private String notificationServiceUrl;

  public void sendExamDocumentUploadNotification(String userId, String fileName) {
    sendNotification(userId, fileName, "Prüfungsleistungsdokument");
  }

  public void sendPubDocumentUploadNotification(String userId, String fileName) {
    sendNotification(userId, fileName, "Prüfungsunfähigkeitsdokument");
  }

  private void sendNotification(String userId, String fileName, String documentType) {
    try {
      NotificationRequest notification =
          NotificationRequest.builder()
              .users(Collections.singletonList(userId))
              .title("Datei erfolgreich hochgeladen")
              .message(fileName + " als " + documentType + " erfolgreich hochgeladen")
              .notifyType("UI")
              .notificationType("Congratulation")
              .build();

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);

      HttpEntity<NotificationRequest> request = new HttpEntity<>(notification, headers);

      notificationRestTemplate.postForObject(notificationServiceUrl, request, Void.class);

      log.info(
          "Notification sent successfully for {} file: {} to user: {}",
          documentType,
          fileName,
          userId);
    } catch (RestClientException e) {
      log.error(
          "Failed to send notification for {} file: {} to user: {}",
          documentType,
          fileName,
          userId,
          e);
    }
  }
}
