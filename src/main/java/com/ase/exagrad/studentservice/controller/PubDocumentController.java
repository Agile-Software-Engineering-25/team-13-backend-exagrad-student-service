package com.ase.exagrad.studentservice.controller;

import com.ase.exagrad.studentservice.component.ApiResponseFactory;
import com.ase.exagrad.studentservice.dto.request.PubDocumentRequest;
import com.ase.exagrad.studentservice.dto.response.ApiResponse;
import com.ase.exagrad.studentservice.dto.response.PubDocumentResponse;
import com.ase.exagrad.studentservice.service.PubDocumentService;
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
@RequestMapping("/documents/pub")
@RequiredArgsConstructor
public class PubDocumentController {

  private final PubDocumentService pubDocumentService;
  private final ApiResponseFactory apiResponseFactory;

  @PostMapping(consumes = {"multipart/form-data"})
  public ResponseEntity<ApiResponse<PubDocumentResponse>> uploadPubDocument(
      @RequestPart("file") MultipartFile file,
      @RequestPart("metadata") PubDocumentRequest metadata,
      HttpServletRequest request) {

    try {
      PubDocumentResponse response = pubDocumentService.uploadPubDocument(file, metadata);

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
  public ResponseEntity<ApiResponse<List<PubDocumentResponse>>> getDocuments(
      @RequestParam(required = false) String studentId,
      HttpServletRequest request) {

    if (studentId == null || studentId.isEmpty()) {
      return ResponseEntity.badRequest()
          .body(
              apiResponseFactory.badRequest(
                  "Parameter studentId is required",
                  request.getRequestURI()));
    }

    List<PubDocumentResponse> documents = pubDocumentService.getDocumentsByStudentId(studentId);

    return ResponseEntity.ok(apiResponseFactory.success(documents, request.getRequestURI()));
  }
}
