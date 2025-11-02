package com.ase.exagrad.studentservice.service.external;

import com.ase.exagrad.studentservice.dto.external.StudentDto;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class StudentService {

  private final WebClient studentWebClient;

  public StudentService(
      WebClient.Builder webClientBuilder,
      @Value("${app.external-apis.student.base-url}") String studentBaseUrl) {
    this.studentWebClient = webClientBuilder.baseUrl(studentBaseUrl).build();
  }

  public List<StudentDto> fetchDataForStudent(String studentId) {
    StudentDto[] student =
        studentWebClient
            .get()
            .uri("/api/students")
            .retrieve()
            .bodyToMono(StudentDto[].class)
            .block();

    return Arrays.asList(Objects.requireNonNull(student));
  }
}
