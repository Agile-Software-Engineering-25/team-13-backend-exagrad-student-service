package com.ase.exagrad.studentservice.dto.response;

import lombok.Data;

@Data
public class FeedbackResponseDto {
    private String grade;
    private String total_points;
    private String comment;
}
