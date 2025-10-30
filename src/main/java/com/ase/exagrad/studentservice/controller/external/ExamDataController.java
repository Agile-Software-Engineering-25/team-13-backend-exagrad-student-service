package com.ase.exagrad.studentservice.controller;

import com.ase.exagrad.studentservice.dto.external.CourseResponseDto;
import com.ase.exagrad.studentservice.service.external.CourseExamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/data")
public class ExamDataController {
  private final CourseExamService courseExamService;

  @GetMapping("/students/{studentId}/courses")
  @Operation(
      summary = "Get student courses",
      description = "Get all courses for a specific student")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Courses retrieved successfully"),
      })
  public ResponseEntity<CourseResponseDto> getStudentCourses(@PathVariable String studentId) {
    CourseResponseDto courses = courseExamService.fetchCoursesForStudent(studentId);
    return ResponseEntity.ok(courses);
  }
}
