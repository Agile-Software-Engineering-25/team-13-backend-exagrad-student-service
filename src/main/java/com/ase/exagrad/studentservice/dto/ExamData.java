package com.ase.exagrad.studentservice.dto;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ExamData {

  private String id;
  private String title;
  private String moduleCode;
  private String examDate;
  private String room;
  private String examType;
  private String semester;
  private String ects;
  private  String maxPoints;
  private String duration;
  private String attemptNumber;
  private boolean fileUploadRequired;
  private String tools;
}
