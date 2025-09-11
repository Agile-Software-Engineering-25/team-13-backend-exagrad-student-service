package com.ase.exagrad.studentservice.dto.response;

import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExamDocumentResponse {
  private UUID id;
  private String examId;
  private String studentId;
  private ZonedDateTime uploadDate;
  private String downloadUrl;
  private String fileName;
}
