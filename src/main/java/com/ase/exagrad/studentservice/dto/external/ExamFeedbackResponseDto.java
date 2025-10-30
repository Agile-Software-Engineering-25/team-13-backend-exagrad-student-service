package com.ase.exagrad.studentservice.dto.external;

import java.util.List;
import lombok.Data;

@Data
public class ExamFeedbackResponseDto {
  private String uuid;
  private String gradedAt;
  private String examUuid;
  private String lecturerUuid;
  private String studentUuid;
  private String submissionUuid;
  private String comment;
  private List<FeedbackFileReferenceDto> fileReference;
  private int points;
  private float grade;
  private String publishStatus;
}
