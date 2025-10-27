package com.ase.exagrad.studentservice.dto;


import lombok.Data;
import java.util.List;

@Data
public class ExamDataDto {

  private String id;
  private String title;
  private String moduleCode;
  private String examDate;
  private String room;
  private String examType;
  private String semester;
  private int ects;
  private int maxPoints;
  private int duration;
  private int attemptNumber;
  private boolean fileUploadRequired;
  private List<String> tools;
}
