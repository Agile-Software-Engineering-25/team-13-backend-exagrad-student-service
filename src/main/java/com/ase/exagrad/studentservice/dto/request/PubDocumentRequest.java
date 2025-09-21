package com.ase.exagrad.studentservice.dto.request;

import lombok.Data;
import java.time.LocalDate;
//import jakarta.validation.constraints.NotNull;

@Data
public class PubDocumentRequest {

  private String studentId;

  //@NotNull(message = "Start date is required")
  //@Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Date must be in YYYY-MM-DD format")
  private LocalDate startDate;

  //@NotNull(message = "End date is required")
  //@Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Date must be in YYYY-MM-DD format")
  private LocalDate endDate;
}
