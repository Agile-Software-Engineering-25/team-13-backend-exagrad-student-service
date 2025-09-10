package com.ase.exagrad.studentservice.controller;

import java.io.IOException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.ase.exagrad.studentservice.component.ApiResponseFactory;
import com.ase.exagrad.studentservice.dto.request.ExamDocumentRequest;
import com.ase.exagrad.studentservice.dto.response.ApiResponse;
import com.ase.exagrad.studentservice.dto.response.ExamDocumentResponse;
import com.ase.exagrad.studentservice.service.ExamDocumentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/documents/exams")
@RequiredArgsConstructor
public class ExamDocumentController {

  private final ExamDocumentService examDocumentService;
  private final ApiResponseFactory apiResponseFactory;

  @PostMapping(consumes = {"multipart/form-data"})
  public ResponseEntity<ApiResponse<ExamDocumentResponse>> uploadExamDocument(
      @RequestPart("file") MultipartFile file,
      @RequestPart("metadata") ExamDocumentRequest metadata,
      HttpServletRequest request) {

    try {
      ExamDocumentResponse response = examDocumentService.uploadExamDocument(file, metadata);

      return ResponseEntity.status(HttpStatus.CREATED)
          .body(apiResponseFactory.created(response, request.getRequestURI()));

    }
    catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest()
          .body(apiResponseFactory.badRequest(e.getMessage(), request.getRequestURI()));
    }
    catch (IOException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(
              apiResponseFactory.internalServerError(
                  "Upload failed: " + e.getMessage(), request.getRequestURI()));
    }
  }

  @GetMapping
  public ResponseEntity<ApiResponse<List<ExamDocumentResponse>>> getDocuments(
      @RequestParam(required = false) String studentId,
      @RequestParam(required = false) String examId,
      HttpServletRequest request) {

    if ((studentId != null && !studentId.isEmpty() && examId != null && !examId.isEmpty())
        || ((studentId == null || studentId.isEmpty()) && (examId == null || examId.isEmpty()))) {
      return ResponseEntity.badRequest()
          .body(
              apiResponseFactory.badRequest(
                  "Provide exactly one parameter: studentId OR examId",
                  request.getRequestURI()));
    }

    List<ExamDocumentResponse> documents =
        (studentId != null && !studentId.isEmpty())
            ? examDocumentService.getDocumentsByStudentId(studentId)
            : examDocumentService.getDocumentsByExamId(examId);

    return ResponseEntity.ok(apiResponseFactory.success(documents, request.getRequestURI()));
  }
}
