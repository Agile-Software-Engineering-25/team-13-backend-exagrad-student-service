package com.ase.exagrad.studentservice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ExamDocumentResponse {
    private String examId;
    private String studentId;
    private Instant uploadDate;
    private String downloadUrl;
    private String fileName;
}
