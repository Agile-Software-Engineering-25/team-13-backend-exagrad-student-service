package com.ase.exagrad.studentservice.controllers;

import com.ase.exagrad.studentservice.dto.ExamDocumentRequest;
import com.ase.exagrad.studentservice.dto.ExamDocumentResponse;
import com.ase.exagrad.studentservice.entities.ExamDocument;
import com.ase.exagrad.studentservice.services.ExamDocumentService;

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
@RequestMapping("/documents/exams")
@RequiredArgsConstructor
public class ExamDocumentController {

    private final ExamDocumentService examDocumentService;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> uploadExamDocument(
            @RequestPart("file") MultipartFile file,
            @RequestPart("metadata") ExamDocumentRequest metadata) {
        try {
            ExamDocument saved = examDocumentService.uploadExamDocument(file, metadata);

            Map<String, Object> response =
                    Map.of(
                            "id", saved.getId(),
                            "fileName",
                                    saved
                                            .getFileName()); // Return clean filename instead of
                                                             // minioKey

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

        List<ExamDocumentResponse> response;
        if (studentId != null) {
            response = examDocumentService.getDocumentsByStudentId(studentId);
        } else if (examId != null) {
            response = examDocumentService.getDocumentsByExamId(examId);
        } else {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Either studentId or examId must be provided"));
        }

        return ResponseEntity.ok(response);
    }
}
