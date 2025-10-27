package com.ase.exagrad.studentservice.dto.response;

import java.util.UUID;
import lombok.Data;

@Data
public class LecturerFeedbackResponseDto {
  private UUID id;
  private UUID examId;
  private UUID examSubmissionsId;
  private UUID lecturerId;
  private FeedbackResponseDto feedback;
}
