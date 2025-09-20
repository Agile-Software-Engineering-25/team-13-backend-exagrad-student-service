package com.ase.exagrad.studentservice.dto.request;

import lombok.Data;

@Data
public class PubDocumentRequest {
  private String studentId;
  private String startDate;
  private String endDate;
}
