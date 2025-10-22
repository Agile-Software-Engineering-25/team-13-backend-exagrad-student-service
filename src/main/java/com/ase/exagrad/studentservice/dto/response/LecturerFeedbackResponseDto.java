package com.example.lecturerfeedback.dto.response;

import java.util.UUID;

public class LecturerFeedbackResponseDto {
    private UUID id;
    private UUID exam_id;
    private UUID exam_submissions_id;
    private UUID lecturer_id;
    private FeedbackResponseDto feedback;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getExam_id() { return exam_id; }
    public void setExam_id(UUID exam_id) { this.exam_id = exam_id; }

    public UUID getExam_submissions_id() { return exam_submissions_id; }
    public void setExam_submissions_id(UUID exam_submissions_id) { this.exam_submissions_id = exam_submissions_id; }

    public UUID getLecturer_id() { return lecturer_id; }
    public void setLecturer_id(UUID lecturer_id) { this.lecturer_id = lecturer_id; }

    public FeedbackResponseDto getFeedback() { return feedback; }
    public void setFeedback(FeedbackResponseDto feedback) { this.feedback = feedback; }
}
