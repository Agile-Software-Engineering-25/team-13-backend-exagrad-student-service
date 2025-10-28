package com.ase.exagrad.studentservice.service.external;

import com.ase.exagrad.studentservice.dto.external.LecturerFeedbackResponseDto;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class FeedbackService {

  private final RestTemplate restTemplate;

  @Value("${app.external-apis.lecturer-feedback.base-url}")
  private String baseUrl;

  public List<LecturerFeedbackResponseDto> getAllFeedbackForStudent(String studentId) {
    // Validate that studentId is strictly alphanumeric (adjust regex as needed)
    if (studentId == null || !studentId.matches("^[a-zA-Z0-9_-]+$")) {
      throw new IllegalArgumentException("Invalid student ID format");
    }
    String url = UriComponentsBuilder.fromHttpUrl(baseUrl).pathSegment(studentId).toUriString();

    LecturerFeedbackResponseDto[] response =
        restTemplate.getForObject(url, LecturerFeedbackResponseDto[].class);
    return Arrays.asList(response != null ? response : new LecturerFeedbackResponseDto[0]);
  }
}
