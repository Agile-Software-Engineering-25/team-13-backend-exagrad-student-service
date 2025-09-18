package com.ase.exagrad.studentservice.dto.request;

import lombok.Data;

//warum ist der dateiname noch rot? ;-;
@Data
public class PubDocumentRequest {
  private String pubId;
  private String studentId;
}
