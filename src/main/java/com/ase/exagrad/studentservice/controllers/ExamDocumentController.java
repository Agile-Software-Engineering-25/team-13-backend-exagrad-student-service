package com.ase.exagrad.studentservice.controllers;

import com.ase.exagrad.studentservice.dto.ExamDocumentRequest;
import com.ase.exagrad.studentservice.dto.ExamDocumentResponse;
import com.ase.exagrad.studentservice.entities.ExamDocument;
import com.ase.exagrad.studentservice.services.ExamDocumentService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/documents/exams")
@RequiredArgsConstructor
public class ExamDocumentController {

    private final ExamDocumentService examDocumentService;
    private final ObjectMapper objectMapper;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> uploadExamDocument(
            @RequestPart("file") MultipartFile file,
            @RequestPart("metadata") ExamDocumentRequest metadata) {
        try {
            ExamDocument saved = examDocumentService.uploadExamDocument(file, metadata);

            Map<String, Object> response =
                    Map.of(
                            "id", saved.getId(),
                            "minioKey", saved.getMinioKey());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Upload failed: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getDocuments(
            @RequestParam(required = false) String studentId,
            @RequestParam(required = false) String examId) {

        if (studentId != null && examId != null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Provide either studentId OR examId, not both"));
        }

        List<ExamDocument> documents;
        if (studentId != null) {
            documents = examDocumentService.getDocumentsByStudentId(studentId);
        } else if (examId != null) {
            documents = examDocumentService.getDocumentsByExamId(examId);
        } else {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Either studentId or examId must be provided"));
        }

        List<ExamDocumentResponse> response =
                documents.stream()
                        .map(doc -> objectMapper.convertValue(doc, ExamDocumentResponse.class))
                        .toList();

        return ResponseEntity.ok(response);
    }
}
