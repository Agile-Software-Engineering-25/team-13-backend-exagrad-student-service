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
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/courses")
public class CourseDataController {

  private final CourseService courseService;
  private final ApiResponseFactory apiResponseFactory;

  @GetMapping
  public ResponseEntity<ApiResponseWrapper<List<CourseDto>>> getCourseData(
      HttpServletRequest request) {
    List<CourseDto> courses = courseService.getCourses();
    return ResponseEntity.ok(apiResponseFactory.success(courses, request.getRequestURI()));
  }
}
