package com.ase.exagrad.studentservice.dto.response;

import java.util.UUID;
import lombok.Data;

@Data
public class LecturerFeedbackResponseDto {
    private UUID id;
    private UUID exam_id;
    private UUID exam_submissions_id;
    private UUID lecturer_id;
    private FeedbackResponseDto feedback;
}
