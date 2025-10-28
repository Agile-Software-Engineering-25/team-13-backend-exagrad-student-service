package com.ase.exagrad.studentservice.dto.external;

import lombok.Data;
import java.util.UUID;

@Data
public class FeedbackFileReferenceDto {
  private UUID fileUuid;
  private String filename;
  private String downloadLink;
}
