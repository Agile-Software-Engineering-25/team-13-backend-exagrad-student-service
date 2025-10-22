package com.example.lecturerfeedback.dto.response;

public class FeedbackResponseDto {
    private String grade;
    private String total_points;
    private String comment;

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public String getTotal_points() { return total_points; }
    public void setTotal_points(String total_points) { this.total_points = total_points; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
