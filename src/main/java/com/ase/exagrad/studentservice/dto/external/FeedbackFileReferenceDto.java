package com.ase.exagrad.studentservice.dto.external;

import java.util.UUID;
import lombok.Data;

@Data
public class FeedbackFileReferenceDto {
  private UUID fileUuid;
  private String filename;
  private String downloadLink;
}
