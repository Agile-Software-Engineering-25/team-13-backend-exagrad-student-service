package com.ase.exagrad.studentservice.dto.request;

import lombok.Data;

@Data
public class ExamDocumentRequest {
  private String examId;
  private String studentId;
}
