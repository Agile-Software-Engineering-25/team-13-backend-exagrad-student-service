package com.ase.exagrad.studentservice.controllers;

import com.ase.exagrad.studentservice.dto.ExamDocumentStudentResponse;
import com.ase.exagrad.studentservice.entities.ExamDocument;
import com.ase.exagrad.studentservice.services.ExamDocumentService;
import com.ase.exagrad.studentservice.dto.ExamDocumentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/documents/exams")
@RequiredArgsConstructor
public class ExamDocumentController {

  private final ExamDocumentService examDocumentService;

  @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
  public ResponseEntity<?> uploadExamDocument(
      @RequestPart("file") MultipartFile file,
      @RequestPart("metadata") ExamDocumentRequest metadata
  ) {
    try {
      ExamDocument saved = examDocumentService.uploadExamDocument(file, metadata);

      Map<String, Object> response = Map.of(
          "id", saved.getId(),
          "status", "uploaded",
          "minioKey", saved.getMinioKey()
      );

      return ResponseEntity.ok(response);

    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
    } catch (IOException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Upload failed: " + e.getMessage()));
    }
  }


  @GetMapping("/student/{studentId}")
  public ResponseEntity<List<ExamDocumentStudentResponse>> getDocumentsForStudent(
      @PathVariable String studentId) {

    List<ExamDocumentStudentResponse> response = examDocumentService
        .getDocumentsByStudentId(studentId)
        .stream()
        .map(doc -> ExamDocumentStudentResponse.builder()
            .id(doc.getId())
            .title(doc.getTitle())
            .examId(doc.getExamId())
            .uploadDate(doc.getUploadDate())
            .minioKey(doc.getMinioKey())
            .build())
        .collect(Collectors.toList());

    return ResponseEntity.ok(response);
  }
}
