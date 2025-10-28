package com.ase.exagrad.studentservice.service.external;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.ase.exagrad.studentservice.dto.external.ExamFeedbackResponseDto;
import lombok.RequiredArgsConstructor;

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
    String url = UriComponentsBuilder.fromHttpUrl(baseUrl).pathSegment(studentId).toUriString();

    // SSRF Prevention: Validate host is as expected
    URI baseUri;
    URI finalUri;
    try {
      baseUri = new URI(baseUrl);
      finalUri = new URI(url);
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException("Malformed URL");
    }
    // Only allow requests to the allowed host
    if (!finalUri.getHost().equalsIgnoreCase(baseUri.getHost())) {
      throw new IllegalArgumentException("SSRF protection: Host mismatch");
    }

    ExamFeedbackResponseDto[] response =
        restTemplate.getForObject(finalUri, ExamFeedbackResponseDto[].class);
    return Arrays.asList(response != null ? response : new ExamFeedbackResponseDto[0]);
  }
}
