package com.ase.exagrad.studentservice.controller;

import com.ase.exagrad.studentservice.component.ApiResponseFactory;
import com.ase.exagrad.studentservice.dto.request.PubDocumentRequest;
import com.ase.exagrad.studentservice.dto.response.ApiResponse;
import com.ase.exagrad.studentservice.dto.response.PubDocumentResponse;
import com.ase.exagrad.studentservice.service.PubDocumentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExamDocumentController.class)
class PubDocumentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private PubDocumentService pubDocumentService;

  @MockitoBean
  private ApiResponseFactory apiResponseFactory;

  private MockMultipartFile mockFile;
  private PubDocumentRequest pubDocumentRequest;
  private PubDocumentResponse pubDocumentResponse;
  private UUID testId;

  @BeforeEach
  void setUp() {
    testId = UUID.randomUUID();

    // Create test file
    mockFile =
        new MockMultipartFile(
            "file", "test.pdf", "application/pdf", "Test PDF content".getBytes());

    // Create test request
    pubDocumentRequest = new PubDocumentRequest();
    pubDocumentRequest.setStudentId("STUDENT123");
    pubDocumentRequest.setStartDate(LocalDate.of(2025, 5, 1));
    pubDocumentRequest.setEndDate(LocalDate.of(2025, 5, 10));

    // Create test response
    pubDocumentResponse =
        PubDocumentResponse.builder()
            .id(testId)
            .studentId("STUDENT123")
            .fileName("test.pdf")
            .uploadDate(Instant.now().atZone(ZoneId.systemDefault()))
            .startDate(LocalDate.of(2025, 5, 1))
            .endDate(LocalDate.of(2025, 5, 10))
            .downloadUrl("http://example.com/download/test.pdf")
            .build();
  }

  @Test
  void uploadPubDocumentValidInputReturnsCreatedStatus() throws Exception {
    // Arrange
    ApiResponse<PubDocumentResponse> successResponse =
        createApiResponse(pubDocumentResponse, "Document uploaded successfully");

    when(pubDocumentService.uploadPubDocument(any(), any())).thenReturn(pubDocumentResponse);
    when(apiResponseFactory.created(
        any(PubDocumentResponse.class), any(String.class)))
        .thenReturn(successResponse);

    // Act & Assert
    mockMvc.perform(
            multipart("/documents/pub")
                .file(mockFile)
                .file(createJsonFile("metadata", pubDocumentRequest)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.data.id").value(testId.toString()))
        .andExpect(jsonPath("$.data.studentId").value("STUDENT123"))
        .andExpect(jsonPath("$.data.startDate").value("2025-05-01"))
        .andExpect(jsonPath("$.data.endDate").value("2025-05-10"))
        .andExpect(jsonPath("$.data.fileName").value("test.pdf"))
        .andExpect(jsonPath("$.data.downloadUrl").exists());
  }

  @Test
  void uploadPubDocumentServiceThrowsInvalidDateRangeExceptionReturnsBadRequest()
      throws Exception {
    // Arrange
    String errorMessage = "Invalid date range";
    ApiResponse<PubDocumentResponse> errorResponse = createErrorApiResponse(errorMessage);

    when(pubDocumentService.uploadPubDocument(any(), any()))
        .thenThrow(new IllegalArgumentException(errorMessage));
    when(apiResponseFactory.<PubDocumentResponse>badRequest(
        eq(errorMessage), any(String.class)))
        .thenReturn(errorResponse);

    // Act & Assert
    mockMvc.perform(
            multipart("/documents/pub")
                .file(mockFile)
                .file(createJsonFile("metadata", pubDocumentRequest)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.data.startDate").value("2025-05-10"))
        .andExpect(jsonPath("$.data.endDate").value("2025-05-01"))
        .andExpect(jsonPath("$.message").value(errorMessage));
  }

  @Test
  void uploadPubDocumentServiceThrowsIOExceptionReturnsInternalServerError() throws Exception {
    // Arrange
    String errorMessage = "Upload failed: File processing error";
    ApiResponse<PubDocumentResponse> errorResponse = createErrorApiResponse(errorMessage);

    when(pubDocumentService.uploadPubDocument(any(), any()))
        .thenThrow(new IOException("File processing error"));
    when(apiResponseFactory.<PubDocumentResponse>internalServerError(
        eq(errorMessage), any(String.class)))
        .thenReturn(errorResponse);

    // Act & Assert
    mockMvc.perform(
            multipart("/documents/pub")
                .file(mockFile)
                .file(createJsonFile("metadata", pubDocumentRequest)))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.message").value(errorMessage));
  }

  @Test
  void getPubDocumentsWithStudentIdReturnsPubDocumentsList() throws Exception {
    // Arrange
    List<PubDocumentResponse> documents = List.of(pubDocumentResponse);
    ApiResponse<List<PubDocumentResponse>> successResponse =
        createApiResponse(documents, "Documents retrieved successfully");

    when(pubDocumentService.getDocumentsByStudentId("STUDENT123")).thenReturn(documents);
    when(apiResponseFactory.<List<PubDocumentResponse>>success(
        any(List.class), any(String.class)))
        .thenReturn(successResponse);

    // Act & Assert
    mockMvc.perform(get("/documents/pub").param("studentId", "STUDENT123"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").isArray())
        .andExpect(jsonPath("$.data[0].id").value(testId.toString()))
        .andExpect(jsonPath("$.data[0].studentId").value("STUDENT123"));
  }

  @Test
  void getPubDocumentsNoParametersReturnsBadRequest() throws Exception {
    // Arrange
    String errorMessage = "Provide studentId";
    ApiResponse<List<PubDocumentResponse>> errorResponse =
        createErrorApiResponse(errorMessage);

    when(apiResponseFactory.<List<PubDocumentResponse>>badRequest(
        eq(errorMessage), any(String.class)))
        .thenReturn(errorResponse);

    // Act & Assert
    mockMvc.perform(get("/documents/pub"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value(errorMessage));
  }

  // Helper methods
  private MockMultipartFile createJsonFile(String name, Object content) throws Exception {
    return new MockMultipartFile(
        name, "", "application/json", objectMapper.writeValueAsBytes(content));
  }

  private <T> ApiResponse<T> createApiResponse(T data, String message) {
    ApiResponse<T> response = new ApiResponse<>();
    response.setData(data);
    response.setMessage(message);
    response.setEndpoint("/documents/pub");
    return response;
  }

  private <T> ApiResponse<T> createErrorApiResponse(String message) {
    ApiResponse<T> response = new ApiResponse<>();
    response.setMessage(message);
    response.setEndpoint("/documents/pub");
    return response;
  }
}
