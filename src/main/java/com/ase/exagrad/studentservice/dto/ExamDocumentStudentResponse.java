package com.ase.exagrad.studentservice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class ExamDocumentStudentResponse {
  private UUID id;
  private String title;
  private String examId;
  private Instant uploadDate;
  private String minioKey;
}
