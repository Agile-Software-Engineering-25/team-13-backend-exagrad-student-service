package com.ase.exagrad.studentservice.dto.external;

import lombok.Data;

@Data
public class CourseResponseDto {

  private String externalId;
  private java.util.List<CourseDto> courses;
}
