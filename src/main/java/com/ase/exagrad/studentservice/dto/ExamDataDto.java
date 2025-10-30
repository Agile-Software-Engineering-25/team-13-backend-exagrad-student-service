package com.ase.exagrad.studentservice.dto;

import java.util.List;
import lombok.Data;

@Data
public class ExamDataDto {

  private String id;
  private String title;
  private String moduleCode;
  private String examDate;
  private String room;
  private String examType;
  private String semester;
  private Integer ects;
  private Integer maxPoints;
  private Integer duration;
  private boolean fileUploadRequired;
  private List<String> tools;
}
