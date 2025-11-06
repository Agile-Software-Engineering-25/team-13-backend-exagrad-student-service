package com.ase.exagrad.studentservice.service.external;

import com.ase.exagrad.studentservice.dto.external.StudentDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class StudentService {

  private final WebClient webClient;

  public StudentService(WebClient.Builder webClientBuilder) {
    this.webClient = webClientBuilder.baseUrl("https://sau-portal.de/team-11-api/api/v1/").build();
  }

  @Value("${app.external-apis.student.base-url}")
  private String studentBaseUrl;

  public StudentDto fetchDataForStudent(String studentId) {
    return webClient
        .get()
        .uri("/users/{userId}", studentId)
        .retrieve()
        .bodyToMono(StudentDto.class)
        .block();
  }
}
