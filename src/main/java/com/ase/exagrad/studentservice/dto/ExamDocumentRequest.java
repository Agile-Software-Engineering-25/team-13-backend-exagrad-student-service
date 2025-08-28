package com.ase.exagrad.studentservice.dto;

import lombok.Data;

@Data
public class ExamDocumentRequest {
  private String title;
  private String examId;
  private String studentId;
}
