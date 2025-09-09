package com.ase.exagrad.studentservice.dtos.response;

import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Builder
public class ExamDocumentResponse {
    private UUID id;
    private String examId;
    private String studentId;
    private ZonedDateTime uploadDate;
    private String downloadUrl;
    private String fileName;
}
