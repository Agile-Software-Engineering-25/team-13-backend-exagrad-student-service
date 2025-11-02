package com.ase.exagrad.studentservice.service.external;

import com.ase.exagrad.studentservice.dto.external.StudentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class StudentService {

  private final WebClient.Builder webClientBuilder;

  @Value("${app.external-apis.student.base-url}")
  private String studentBaseUrl;

  public StudentDto fetchDataForStudent(String studentId) {
    return webClientBuilder
        .baseUrl(studentBaseUrl)
        .build()
        .get()
        .uri("/users/{studentId}", studentId)
        .retrieve()
        .bodyToMono(StudentDto.class)
        .block();
  }
}
