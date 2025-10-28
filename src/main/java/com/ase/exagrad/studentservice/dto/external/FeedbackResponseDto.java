package com.ase.exagrad.studentservice.dto.external;

import lombok.Data;

@Data
public class FeedbackResponseDto {
  private String grade;
  private String totalPoints;
  private String comment;
}
