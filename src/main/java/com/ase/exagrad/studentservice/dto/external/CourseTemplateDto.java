package com.ase.exagrad.studentservice.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CourseTemplateDto {

  private Long id;
  private String name;
  private String code;
  private Boolean elective;

  @JsonProperty("planned_semester")
  private Integer plannedSemester;
}
