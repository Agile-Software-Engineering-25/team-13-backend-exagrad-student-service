package com.ase.exagrad.studentservice.dto.external;

import lombok.Data;

@Data
public class CourseDto {
  private int semester;
  private String examType;
  private int creditPoints;
  private int totalUnits;
  private int templateId;
  private int id;
  private TemplateDto template;
}
