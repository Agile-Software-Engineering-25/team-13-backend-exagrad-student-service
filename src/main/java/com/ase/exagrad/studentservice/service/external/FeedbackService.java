package com.ase.exagrad.studentservice.service.external;

import com.ase.exagrad.studentservice.dto.external.ExamFeedbackResponseDto;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedbackService {

  private final RestTemplate restTemplate;

  @Value("${app.external-apis.lecturer-feedback.base-url}")
  private String baseUrl;

  public List<ExamFeedbackResponseDto> getAllFeedbackForStudent(String studentId) {
    // Validate that studentId is strictly alphanumeric (adjust regex as needed)
    if (studentId == null || !studentId.matches("^[a-zA-Z0-9_-]+$")) {
      throw new IllegalArgumentException("Invalid student ID format");
    }

    // SSRF Prevention: Build URI directly without intermediate string conversion
    URI baseUri;
    URI finalUri;
    try {
      baseUri = new URI(baseUrl);
      finalUri =
          UriComponentsBuilder.fromHttpUrl(baseUrl).pathSegment(studentId).build(true).toUri();
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException("Malformed URL");
    }

    // SSRF Prevention: Validate scheme, host, and no query/fragment
    if (!finalUri.getScheme().equalsIgnoreCase(baseUri.getScheme())) {
      throw new IllegalArgumentException("SSRF protection: Scheme mismatch");
    }
    if (!finalUri.getHost().equalsIgnoreCase(baseUri.getHost())) {
      throw new IllegalArgumentException("SSRF protection: Host mismatch");
    }
    if (finalUri.getQuery() != null || finalUri.getFragment() != null) {
      throw new IllegalArgumentException("SSRF protection: Query or fragment not allowed");
    }

    try {
      ExamFeedbackResponseDto[] response =
          restTemplate.getForObject(finalUri, ExamFeedbackResponseDto[].class);

      List<ExamFeedbackResponseDto> approvedFeedbacks =
          filterApprovedFeedbacks(
              Arrays.asList(response != null ? response : new ExamFeedbackResponseDto[0]));
      log.info(
          "Fetched {} approved feedback entries for student {}",
          approvedFeedbacks.size(),
          studentId);
      return approvedFeedbacks;
    } catch (RestClientException e) {
      log.error("Failed to fetch feedback for student {}: {}", studentId, e.getMessage(), e);
      throw new RuntimeException("Failed to retrieve feedback from external service", e);
    }
  }

  /*
   * Helper Methods to filter feedback array with publishStatus 'APPROVED'
   */
  public List<ExamFeedbackResponseDto> filterApprovedFeedbacks(
      List<ExamFeedbackResponseDto> feedbacks) {
    return feedbacks.stream()
        .filter(feedback -> "APPROVED".equalsIgnoreCase(feedback.getPublishStatus()))
        .toList();
  }
}
