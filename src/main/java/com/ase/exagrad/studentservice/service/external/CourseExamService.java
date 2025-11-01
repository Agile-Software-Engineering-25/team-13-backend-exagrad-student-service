package com.ase.exagrad.studentservice.service.external;

import com.ase.exagrad.studentservice.dto.ExamDataDto;
import com.ase.exagrad.studentservice.dto.external.CourseDto;
import com.ase.exagrad.studentservice.dto.external.CourseResponseDto;
import com.ase.exagrad.studentservice.dto.external.StudentCourseExamDto;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class CourseExamService {

  private final WebClient courseWebClient;
  private final WebClient examWebClient;

  public CourseExamService(
      WebClient.Builder webClientBuilder,
      @Value("${app.external-apis.course-service.base-url}") String courseBaseUrl,
      @Value("${app.external-apis.exam-service.base-url}") String examBaseUrl) {
    this.courseWebClient = webClientBuilder.baseUrl(courseBaseUrl).build();
    this.examWebClient = webClientBuilder.baseUrl(examBaseUrl).build();
  }

  public List<StudentCourseExamDto> fetchCoursesWithExamsForStudent(String studentId) {
    CourseResponseDto courseResponse =
        courseWebClient
            .get()
            .uri("/students/{studentId}/courses", studentId)
            .retrieve()
            .bodyToMono(CourseResponseDto.class)
            .block();

    List<ExamDataDto> exams =
        examWebClient
            .get()
            .uri("/api/exams")
            .retrieve()
            .bodyToFlux(ExamDataDto.class)
            .collectList()
            .block();

    Map<String, List<ExamDataDto>> examsByModuleCode =
        exams.stream().collect(Collectors.groupingBy(ExamDataDto::getModuleCode));

    return courseResponse.getCourses().stream()
        .map(course -> mapToCourseExamDto(course, examsByModuleCode))
        .collect(Collectors.toList());
  }

  private StudentCourseExamDto mapToCourseExamDto(
      CourseDto course, Map<String, List<ExamDataDto>> examsByModuleCode) {
    return StudentCourseExamDto.builder()
        .courseName(course.getTemplate().getName())
        .courseCode(course.getTemplate().getCode())
        .lecturer("Test Doz")
        .semester(course.getTemplate().getSemester())
        .creditPoints(course.getCreditPoints())
        .exams(examsByModuleCode.getOrDefault(course.getTemplate().getCode(), List.of()))
        .build();
  }
}
