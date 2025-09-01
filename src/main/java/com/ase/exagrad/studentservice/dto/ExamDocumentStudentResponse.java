package com.ase.exagrad.studentservice.dto;

import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExamDocumentStudentResponse {
  private UUID id;
  private String title;
  private String examId;
  private Instant uploadDate;
  private String minioKey;
}
