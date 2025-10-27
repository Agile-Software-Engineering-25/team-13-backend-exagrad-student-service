package com.ase.exagrad.studentservice.controller;

import com.ase.exagrad.studentservice.component.ApiResponseFactory;
import com.ase.exagrad.studentservice.dto.request.ExamDocumentRequest;
import com.ase.exagrad.studentservice.dto.response.ApiResponseWrapper;
import com.ase.exagrad.studentservice.dto.response.ErrorDetails;
import com.ase.exagrad.studentservice.dto.response.ExamDocumentResponse;
import com.ase.exagrad.studentservice.service.ExamDocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/documents/exams")
@RequiredArgsConstructor
@Tag(name = "Exam Documents", description = "Operations for managing exam documents")
public class ExamDocumentController {

  private final ExamDocumentService examDocumentService;
  private final ApiResponseFactory apiResponseFactory;

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Operation(
      summary = "Upload exam document",
      description = "Upload an exam document with metadata")
  @RequestBody(
      content =
          @Content(
              mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
              encoding = {
                @Encoding(name = "metadata", contentType = MediaType.APPLICATION_JSON_VALUE)
              }))
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "201",
            description = "Document uploaded successfully",
            content = @Content(schema = @Schema(implementation = ExamDocumentResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
      })
  public ResponseEntity<ApiResponseWrapper<ExamDocumentResponse>> uploadExamDocument(
      @Parameter(description = "Document file to upload") @RequestPart("file") MultipartFile file,
      @Parameter(description = "Document metadata as JSON") @RequestPart("metadata")
          ExamDocumentRequest metadata,
      HttpServletRequest request) {

    try {
      ExamDocumentResponse response = examDocumentService.uploadExamDocument(file, metadata);

      return ResponseEntity.status(HttpStatus.CREATED)
          .body(apiResponseFactory.created(response, request.getRequestURI()));

    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest()
          .body(apiResponseFactory.badRequest(e.getMessage(), request.getRequestURI()));
    } catch (IOException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(
              apiResponseFactory.internalServerError(
                  "Upload failed: " + e.getMessage(), request.getRequestURI()));
    }
  }

  @GetMapping
  @Operation(
      summary = "Get exam documents",
      description = "Get exam documents by student ID or exam ID")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Documents retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid parameters")
      })
  public ResponseEntity<ApiResponseWrapper<List<ExamDocumentResponse>>> getDocuments(
      @Parameter(description = "Student ID to filter documents") @RequestParam(required = false)
          String studentId,
      @Parameter(description = "Exam ID to filter documents") @RequestParam(required = false)
          String examId,
      HttpServletRequest request) {

    if ((studentId != null && !studentId.isEmpty() && examId != null && !examId.isEmpty())
        || ((studentId == null || studentId.isEmpty()) && (examId == null || examId.isEmpty()))) {
      return ResponseEntity.badRequest()
          .body(
              apiResponseFactory.badRequest(
                  "Provide exactly one parameter: studentId OR examId", request.getRequestURI()));
    }

    List<ExamDocumentResponse> documents =
        (studentId != null && !studentId.isEmpty())
            ? examDocumentService.getDocumentsByStudentId(studentId)
            : examDocumentService.getDocumentsByExamId(examId);

    return ResponseEntity.ok(apiResponseFactory.success(documents, request.getRequestURI()));
  }

  @DeleteMapping("/{documentId}")
  @Operation(
      summary = "Delete exam document",
      description = "Delete an exam document by ID if deadline has not passed")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Document deleted successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid document ID"),
        @ApiResponse(
            responseCode = "403",
            description = "Deadline has passed, deletion not allowed"),
        @ApiResponse(responseCode = "404", description = "Document not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
      })
  public ResponseEntity<ApiResponseWrapper<Void>> deleteExamDocument(
      @Parameter(description = "Document ID to delete") @PathVariable String documentId,
      HttpServletRequest request) {

    try {
      examDocumentService.deleteExamDocument(documentId);
      return ResponseEntity.noContent().build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest()
          .body(apiResponseFactory.badRequest(e.getMessage(), request.getRequestURI()));
    } catch (IllegalStateException e) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN)
          .body(
              apiResponseFactory.error(
                  e.getMessage(),
                  request.getRequestURI(),
                  HttpStatus.FORBIDDEN,
                  ErrorDetails.builder().code("FORBIDDEN").message(e.getMessage()).build()));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(
              apiResponseFactory.internalServerError(
                  "Deletion failed: " + e.getMessage(), request.getRequestURI()));
    }
  }
}
