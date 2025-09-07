package com.ase.exagrad.studentservice.dto.response;

import com.ase.exagrad.studentservice.entities.ExamDocument;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class ExamDocumentResponse {
    private UUID id;
    private String examId;
    private String studentId;
    private Instant uploadDate;
    private String downloadUrl;
    private String fileName;

    public static ExamDocumentResponse fromEntity(ExamDocument doc, String downloadUrl) {
        return ExamDocumentResponse.builder()
                .id(doc.getId())
                .examId(doc.getExamId())
                .studentId(doc.getStudentId())
                .uploadDate(doc.getUploadDate())
                .downloadUrl(downloadUrl)
                .fileName(doc.getFileName())
                .build();
    }
}
