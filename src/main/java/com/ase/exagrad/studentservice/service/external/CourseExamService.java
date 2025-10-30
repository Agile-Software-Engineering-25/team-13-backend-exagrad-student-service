package com.ase.exagrad.studentservice.service.external;

import com.ase.exagrad.studentservice.dto.external.CourseResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class CourseExamService {

  private final WebClient webClient;

  public CourseExamService(WebClient.Builder webClientBuilder) {
    this.webClient =
        webClientBuilder
            .baseUrl("https://sau-portal.de/api/masterdata/studies/external_connections")
            .build();
  }

  public CourseResponseDto fetchCoursesForStudent(String studentId) {
    return webClient
        .get()
        .uri("/students/{studentId}/courses", studentId)
        .retrieve()
        .bodyToMono(CourseResponseDto.class)
        .block();
  }
}
