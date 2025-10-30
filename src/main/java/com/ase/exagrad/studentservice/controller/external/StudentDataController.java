package com.ase.exagrad.studentservice.controller.external;

import com.ase.exagrad.studentservice.component.ApiResponseFactory;
import com.ase.exagrad.studentservice.dto.external.StudentDto;
import com.ase.exagrad.studentservice.dto.response.ApiResponseWrapper;
import com.ase.exagrad.studentservice.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/students")
@Tag(name = "Student Data", description = "Operations for retrieving student information")
public class StudentDataController {

  private final StudentService studentService;
  private final ApiResponseFactory apiResponseFactory;

  @GetMapping
  @Operation(
      summary = "Get data for student",
      description = "Retrieves all available data of this student")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved student data",
            content = @Content(schema = @Schema(implementation = ApiResponseWrapper.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error")
      })
  public ResponseEntity<ApiResponseWrapper<List<StudentDto>>> getStudentData(
      HttpServletRequest request) {
    List<StudentDto> student = studentService.getStudent();
    return ResponseEntity.ok(apiResponseFactory.success(student, request.getRequestURI()));
  }
}
