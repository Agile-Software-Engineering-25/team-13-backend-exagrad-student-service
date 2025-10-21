package com.ase.exagrad.studentservice.service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.ase.exagrad.studentservice.dto.external.CourseDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CourseService {

  private final RestTemplate restTemplate;

  @Value("${app.external-apis.course.base-url}")
  private String coursesApiBaseUrl;

  public List<CourseDto> getCourses() {
    CourseDto[] courses = restTemplate.getForObject(coursesApiBaseUrl, CourseDto[].class);
    return Arrays.asList(Objects.requireNonNull(courses));
  }
}
