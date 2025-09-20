package com.ase.exagrad.studentservice.dto.response;

import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PubDocumentResponse {
  private UUID id;
  private String studentId;
  private ZonedDateTime uploadDate;
  private String startDate;
  private String endDate;
  private String downloadUrl;
  private String fileName;
}
