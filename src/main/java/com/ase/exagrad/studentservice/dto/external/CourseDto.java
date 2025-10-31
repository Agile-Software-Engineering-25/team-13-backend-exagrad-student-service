package com.ase.exagrad.studentservice.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CourseDto {

  private Long id;
  private Integer semester;

  @JsonProperty("exam_type")
  private String examType;

  @JsonProperty("credit_points")
  private Integer creditPoints;

  @JsonProperty("total_units")
  private Integer totalUnits;

  @JsonProperty("template_id")
  private Long templateId;

  private CourseTemplateDto template;
}
