package com.ase.exagrad.studentservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.ase.exagrad.studentservice.component.ApiResponseFactory;
import com.ase.exagrad.studentservice.dto.request.ExamDocumentRequest;
import com.ase.exagrad.studentservice.dto.response.ApiResponse;
import com.ase.exagrad.studentservice.dto.response.ExamDocumentResponse;
import com.ase.exagrad.studentservice.service.ExamDocumentService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ExamDocumentController.class)
class ExamDocumentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private ExamDocumentService examDocumentService;

  @MockitoBean
  private ApiResponseFactory apiResponseFactory;

  private MockMultipartFile mockFile;
  private ExamDocumentRequest examDocumentRequest;
  private ExamDocumentResponse examDocumentResponse;
  private UUID testId;

  @BeforeEach
  void setUp() {
    testId = UUID.randomUUID();

    // Create test file
    mockFile =
        new MockMultipartFile(
            "file", "test.pdf", "application/pdf", "Test PDF content".getBytes());

    // Create test request
    examDocumentRequest = new ExamDocumentRequest();
    examDocumentRequest.setExamId("EXAM123");
    examDocumentRequest.setStudentId("STUDENT123");

    // Create test response
    examDocumentResponse =
        ExamDocumentResponse.builder()
            .id(testId)
            .examId("EXAM123")
            .studentId("STUDENT123")
            .fileName("test.pdf")
            .uploadDate(Instant.now().atZone(ZoneId.systemDefault()))
            .downloadUrl("http://example.com/download/test.pdf")
            .build();
  }

  @Test
  void uploadExamDocumentValidInputReturnsCreatedStatus() throws Exception {
    // Arrange
    ApiResponse<ExamDocumentResponse> successResponse =
        createApiResponse(examDocumentResponse, "Document uploaded successfully");

    when(examDocumentService.uploadExamDocument(any(), any())).thenReturn(examDocumentResponse);
    when(apiResponseFactory.created(
        any(ExamDocumentResponse.class), any(String.class)))
        .thenReturn(successResponse);

    // Act & Assert
    mockMvc.perform(
            multipart("/documents/exams")
                .file(mockFile)
                .file(createJsonFile("metadata", examDocumentRequest)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.data.id").value(testId.toString()))
        .andExpect(jsonPath("$.data.examId").value("EXAM123"))
        .andExpect(jsonPath("$.data.studentId").value("STUDENT123"))
        .andExpect(jsonPath("$.data.fileName").value("test.pdf"))
        .andExpect(jsonPath("$.data.downloadUrl").exists());
  }

  @Test
  void uploadExamDocumentServiceThrowsIllegalArgumentExceptionReturnsBadRequest()
      throws Exception {
    // Arrange
    String errorMessage = "Invalid file format";
    ApiResponse<ExamDocumentResponse> errorResponse = createErrorApiResponse(errorMessage);

    when(examDocumentService.uploadExamDocument(any(), any()))
        .thenThrow(new IllegalArgumentException(errorMessage));
    when(apiResponseFactory.<ExamDocumentResponse>badRequest(
        eq(errorMessage), any(String.class)))
        .thenReturn(errorResponse);

    // Act & Assert
    mockMvc.perform(
            multipart("/documents/exams")
                .file(mockFile)
                .file(createJsonFile("metadata", examDocumentRequest)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value(errorMessage));
  }

  @Test
  void uploadExamDocumentServiceThrowsIOExceptionReturnsInternalServerError() throws Exception {
    // Arrange
    String errorMessage = "Upload failed: File processing error";
    ApiResponse<ExamDocumentResponse> errorResponse = createErrorApiResponse(errorMessage);

    when(examDocumentService.uploadExamDocument(any(), any()))
        .thenThrow(new IOException("File processing error"));
    when(apiResponseFactory.<ExamDocumentResponse>internalServerError(
        eq(errorMessage), any(String.class)))
        .thenReturn(errorResponse);

    // Act & Assert
    mockMvc.perform(
            multipart("/documents/exams")
                .file(mockFile)
                .file(createJsonFile("metadata", examDocumentRequest)))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.message").value(errorMessage));
  }

  @Test
  void getDocumentsWithStudentIdReturnsDocumentsList() throws Exception {
    // Arrange
    List<ExamDocumentResponse> documents = List.of(examDocumentResponse);
    ApiResponse<List<ExamDocumentResponse>> successResponse =
        createApiResponse(documents, "Documents retrieved successfully");

    when(examDocumentService.getDocumentsByStudentId("STUDENT123")).thenReturn(documents);
    when(apiResponseFactory.<List<ExamDocumentResponse>>success(
        any(List.class), any(String.class)))
        .thenReturn(successResponse);

    // Act & Assert
    mockMvc.perform(get("/documents/exams").param("studentId", "STUDENT123"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").isArray())
        .andExpect(jsonPath("$.data[0].id").value(testId.toString()))
        .andExpect(jsonPath("$.data[0].studentId").value("STUDENT123"));
  }

  @Test
  void getDocumentsWithExamIdReturnsDocumentsList() throws Exception {
    // Arrange
    List<ExamDocumentResponse> documents = List.of(examDocumentResponse);
    ApiResponse<List<ExamDocumentResponse>> successResponse =
        createApiResponse(documents, "Documents retrieved successfully");

    when(examDocumentService.getDocumentsByExamId("EXAM123")).thenReturn(documents);
    when(apiResponseFactory.<List<ExamDocumentResponse>>success(
        any(List.class), any(String.class)))
        .thenReturn(successResponse);

    // Act & Assert
    mockMvc.perform(get("/documents/exams").param("examId", "EXAM123"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").isArray())
        .andExpect(jsonPath("$.data[0].id").value(testId.toString()))
        .andExpect(jsonPath("$.data[0].examId").value("EXAM123"));
  }

  @Test
  void getDocumentsNoParametersReturnsBadRequest() throws Exception {
    // Arrange
    String errorMessage = "Provide exactly one parameter: studentId OR examId";
    ApiResponse<List<ExamDocumentResponse>> errorResponse =
        createErrorApiResponse(errorMessage);

    when(apiResponseFactory.<List<ExamDocumentResponse>>badRequest(
        eq(errorMessage), any(String.class)))
        .thenReturn(errorResponse);

    // Act & Assert
    mockMvc.perform(get("/documents/exams"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value(errorMessage));
  }

  @Test
  void getDocumentsBothParametersReturnsBadRequest() throws Exception {
    // Arrange
    String errorMessage = "Provide exactly one parameter: studentId OR examId";
    ApiResponse<List<ExamDocumentResponse>> errorResponse =
        createErrorApiResponse(errorMessage);

    when(apiResponseFactory.<List<ExamDocumentResponse>>badRequest(
        eq(errorMessage), any(String.class)))
        .thenReturn(errorResponse);

    // Act & Assert
    mockMvc.perform(
            get("/documents/exams")
                .param("studentId", "STUDENT123")
                .param("examId", "EXAM123"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value(errorMessage));
  }

  @Test
  void getDocumentsEmptyStudentIdReturnsBadRequest() throws Exception {
    // Arrange
    String errorMessage = "Provide exactly one parameter: studentId OR examId";
    ApiResponse<List<ExamDocumentResponse>> errorResponse =
        createErrorApiResponse(errorMessage);

    when(apiResponseFactory.<List<ExamDocumentResponse>>badRequest(
        eq(errorMessage), any(String.class)))
        .thenReturn(errorResponse);

    // Act & Assert
    mockMvc.perform(get("/documents/exams").param("studentId", ""))
        .andExpect(status().isBadRequest());
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
    response.setEndpoint("/documents/exams");
    return response;
  }

  private <T> ApiResponse<T> createErrorApiResponse(String message) {
    ApiResponse<T> response = new ApiResponse<>();
    response.setMessage(message);
    response.setEndpoint("/documents/exams");
    return response;
  }
}
