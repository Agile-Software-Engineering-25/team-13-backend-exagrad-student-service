package com.ase.exagrad.studentservice.controllers;

import com.ase.exagrad.studentservice.dto.request.ExamDocumentRequest;
import com.ase.exagrad.studentservice.dto.response.ExamDocumentResponse;
import com.ase.exagrad.studentservice.dto.response.UploadResponse;
import com.ase.exagrad.studentservice.dto.response.ApiResponse;
import com.ase.exagrad.studentservice.entities.ExamDocument;
import com.ase.exagrad.studentservice.services.ExamDocumentService;

import jakarta.servlet.http.HttpServletRequest;

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

@RestController
@RequestMapping("/documents/exams")
@RequiredArgsConstructor
public class ExamDocumentController {

    private final ExamDocumentService examDocumentService;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ApiResponse<UploadResponse>> uploadExamDocument(
            @RequestPart("file") MultipartFile file,
            @RequestPart("metadata") ExamDocumentRequest metadata,
            HttpServletRequest request) {

        try {
            ExamDocument saved = examDocumentService.uploadExamDocument(file, metadata);

            UploadResponse uploadResponse = UploadResponse.fromEntity(saved);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.created(uploadResponse, request.getRequestURI()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest(e.getMessage(), request.getRequestURI()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            ApiResponse.internalServerError(
                                    "Upload failed: " + e.getMessage(), request.getRequestURI()));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ExamDocumentResponse>>> getDocuments(
            @RequestParam(required = false) String studentId,
            @RequestParam(required = false) String examId,
            HttpServletRequest request) {

        if ((studentId != null && examId != null) || (studentId == null && examId == null)) {
            return ResponseEntity.badRequest()
                    .body(
                            ApiResponse.badRequest(
                                    "Provide exactly one parameter: studentId OR examId",
                                    request.getRequestURI()));
        }

        List<ExamDocumentResponse> documents =
                studentId != null
                        ? examDocumentService.getDocumentsByStudentId(studentId)
                        : examDocumentService.getDocumentsByExamId(examId);

        return ResponseEntity.ok(ApiResponse.success(documents, request.getRequestURI()));
    }
}
