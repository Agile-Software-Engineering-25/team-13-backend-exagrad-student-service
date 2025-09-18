package com.ase.exagrad.studentservice.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.ZonedDateTime;
import java.util.UUID;

//warum ist der dateiname noch rot? ;-;
@Data
@Builder
public class PubDocumentResponse {
  private UUID id;
  private String pubId;
  private String studentId;
  private ZonedDateTime uploadDate;
  private String startDate; //change to another type?
  private String endDate; //change to another type?
  private String downloadUrl;
  private String fileName;
}
