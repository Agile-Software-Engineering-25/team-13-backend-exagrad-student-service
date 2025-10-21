package com.ase.exagrad.studentservice.controller.external;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ase.exagrad.studentservice.component.ApiResponseFactory;
import com.ase.exagrad.studentservice.dto.external.CourseDto;
import com.ase.exagrad.studentservice.dto.response.ApiResponseWrapper;
import com.ase.exagrad.studentservice.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/courses")
@Tag(name = "Course Data", description = "Operations for retrieving course information")
public class CourseDataController {

  private final CourseService courseService;
  private final ApiResponseFactory apiResponseFactory;

  @GetMapping
  @Operation(
      summary = "Get all courses",
      description = "Retrieves a list of all available courses")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Successfully retrieved course list",
          content = @Content(schema = @Schema(implementation = ApiResponseWrapper.class))),
      @ApiResponse(
          responseCode = "500",
          description = "Internal server error")
  })
  public ResponseEntity<ApiResponseWrapper<List<CourseDto>>> getCourseData(
      HttpServletRequest request) {
    List<CourseDto> courses = courseService.getCourses();
    return ResponseEntity.ok(apiResponseFactory.success(courses, request.getRequestURI()));
  }
}
