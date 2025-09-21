package com.ase.exagrad.studentservice.dto.request;

import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PubDocumentRequest {

  private String studentId;

  @NotNull(message = "Start date is required")
  private LocalDate startDate;

  @NotNull(message = "End date is required")
  private LocalDate endDate;
}
