package com.ase.exagrad.studentservice.service.external;

import com.ase.exagrad.studentservice.dto.external.StudentDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class StudentService {

  private final WebClient studentWebClient;

  public StudentService(
      WebClient.Builder webClientBuilder,
      @Value("${app.external-apis.student.base-url}") String studentBaseUrl) {
    this.studentWebClient = webClientBuilder.baseUrl(studentBaseUrl).build();
  }

  public StudentDto fetchDataForStudent(String studentId) {
    return studentWebClient
        .get()
        .uri("/users/{studentId}", studentId)
        .retrieve()
        .bodyToMono(StudentDto.class)
        .block();
  }
}
