package com.ase.exagrad.studentservice.controller;

import com.ase.exagrad.studentservice.component.ApiResponseFactory;
import com.ase.exagrad.studentservice.dto.request.PubDocumentRequest;
import com.ase.exagrad.studentservice.dto.response.ApiResponseWrapper;
import com.ase.exagrad.studentservice.dto.response.PubDocumentResponse;
import com.ase.exagrad.studentservice.exception.InvalidDateRangeException;
import com.ase.exagrad.studentservice.service.PubDocumentService;
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
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/documents/pub")
@RequiredArgsConstructor
@Tag(name = "PUB Documents", description = "Operations for managing Prüfungsunfähigkeitsdokumente")
public class PubDocumentController {

  private final PubDocumentService pubDocumentService;
  private final ApiResponseFactory apiResponseFactory;

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Operation(
      summary = "Upload PUB document",
      description = "Upload a Prüfungsunfähigkeitsdokument with metadata")
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
            content = @Content(schema = @Schema(implementation = PubDocumentResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
      })
  public ResponseEntity<ApiResponseWrapper<PubDocumentResponse>> uploadPubDocument(
      @Parameter(description = "PUB document file to upload") @RequestPart("file")
          MultipartFile file,
      @Parameter(description = "Document metadata") @Valid @RequestPart("metadata")
          PubDocumentRequest metadata,
      HttpServletRequest request) {

    try {
      PubDocumentResponse response = pubDocumentService.uploadPubDocument(file, metadata);

      return ResponseEntity.status(HttpStatus.CREATED)
          .body(apiResponseFactory.created(response, request.getRequestURI()));

    } catch (InvalidDateRangeException e) {
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
      summary = "Get PUB documents",
      description = "Get Prüfungsunfähigkeitsdokumente by student ID")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Documents retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Student ID is required")
      })
  public ResponseEntity<ApiResponseWrapper<List<PubDocumentResponse>>> getDocuments(
      @Parameter(description = "Student ID to filter documents", required = true)
          @RequestParam(required = false)
          String studentId,
      HttpServletRequest request) {

    if (studentId == null || studentId.isEmpty()) {
      return ResponseEntity.badRequest()
          .body(
              apiResponseFactory.badRequest(
                  "Parameter studentId is required", request.getRequestURI()));
    }

    List<PubDocumentResponse> documents = pubDocumentService.getDocumentsByStudentId(studentId);

    return ResponseEntity.ok(apiResponseFactory.success(documents, request.getRequestURI()));
  }
}
