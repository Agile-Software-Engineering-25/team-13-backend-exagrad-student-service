package com.ase.exagrad.studentservice.service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.ase.exagrad.studentservice.dto.external.StudentDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class StudentService {
  private final RestTemplate restTemplate;

  @Value("${app.external-apis.student.base-url}")
  private String studentApiBaseUrl;

  public List<StudentDto> getStudent() {
    StudentDto[] student = restTemplate.getForObject(studentApiBaseUrl, StudentDto[].class);
    return Arrays.asList(Objects.requireNonNull(student));
  }
}
