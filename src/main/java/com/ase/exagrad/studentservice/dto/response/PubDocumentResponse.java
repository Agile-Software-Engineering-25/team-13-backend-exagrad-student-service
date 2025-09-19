package com.ase.exagrad.studentservice.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Builder
public class PubDocumentResponse {
  private UUID id;
  private String pubId;
  private String studentId;
  private ZonedDateTime uploadDate;
  private String startDate;
  private String endDate;
  private String downloadUrl;
  private String fileName;
}
