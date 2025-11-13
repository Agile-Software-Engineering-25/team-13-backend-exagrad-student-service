package com.ase.exagrad.studentservice.controller.external;

import com.ase.exagrad.studentservice.component.ApiResponseFactory;
import com.ase.exagrad.studentservice.dto.StudentDataDto;
import com.ase.exagrad.studentservice.dto.response.ApiResponseWrapper;
import com.ase.exagrad.studentservice.service.StudentDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Student Data", description = "Operations for retrieving student information")
public class StudentDataController {

  private final StudentDataService studentDataService;
  private final ApiResponseFactory apiResponseFactory;

  @GetMapping("/{studentId}")
  @Operation(
      summary = "Get all data for a student",
      description =
          "Retrieves all available data for a student, including personal information, courses, and exams.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved student data",
            content = @Content(schema = @Schema(implementation = ApiResponseWrapper.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error")
      })
  public ResponseEntity<ApiResponseWrapper<StudentDataDto>> getStudentData(
      @PathVariable String studentId, HttpServletRequest request) {
    String authorizationHeader = request.getHeader("Authorization");
    StudentDataDto studentData = studentDataService.getStudentData(studentId, authorizationHeader);
    return ResponseEntity.ok(apiResponseFactory.success(studentData, request.getRequestURI()));
  }
}
