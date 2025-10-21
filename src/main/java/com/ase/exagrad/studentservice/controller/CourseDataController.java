package com.ase.exagrad.studentservice.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ase.exagrad.studentservice.dto.external.CourseDto;
import com.ase.exagrad.studentservice.service.CourseService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/courses")
public class CourseDataController {

  private final CourseService courseService;

  @GetMapping
  public List<CourseDto> getCourseData() {
    return courseService.getCourses();
  }
}
