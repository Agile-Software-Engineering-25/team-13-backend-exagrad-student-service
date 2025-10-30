package com.ase.exagrad.studentservice.controller.external;

import com.ase.exagrad.studentservice.dto.external.StudentCourseExamDto;
import com.ase.exagrad.studentservice.service.external.CourseExamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
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
      summary = "Get student courses with exams",
      description = "Get all courses for a specific student enriched with exam data")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Courses with exams retrieved successfully"),
      })
  public ResponseEntity<List<StudentCourseExamDto>> getStudentCourses(
      @PathVariable String studentId) {
    List<StudentCourseExamDto> courses =
        courseExamService.fetchCoursesWithExamsForStudent(studentId);
    return ResponseEntity.ok(courses);
  }
}
