package com.ase.exagrad.studentservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.unit.DataSize;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import com.ase.exagrad.studentservice.component.ApiResponseFactory;
import com.ase.exagrad.studentservice.config.FileProperties;
import com.ase.exagrad.studentservice.dto.response.ApiResponse;
import com.ase.exagrad.studentservice.dto.response.ErrorDetails;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.ase.exagrad.studentservice.exception.InvalidDateRangeException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
  private final ApiResponseFactory apiResponseFactory;
  private final FileProperties fileProperties;

  @ExceptionHandler(FileValidationException.class)
  public ResponseEntity<ApiResponse<Void>> handleFileValidation(
      FileValidationException ex, HttpServletRequest request) {
    log.warn("File validation error: {}", ex.getMessage());

    ErrorDetails error =
        ErrorDetails.builder()
            .code("FILE_VALIDATION_ERROR")
            .message(ex.getMessage())
            .build();

    return ResponseEntity.badRequest()
        .body(
            apiResponseFactory.error(
                ex.getMessage(),
                request.getRequestURI(),
                HttpStatus.BAD_REQUEST,
                error));
  }

  @ExceptionHandler(StorageException.class)
  public ResponseEntity<ApiResponse<Void>> handleStorage(
      StorageException ex, HttpServletRequest request) {
    log.error("Storage error: {}", ex.getMessage(), ex);

    ErrorDetails error =
        ErrorDetails.builder()
            .code("STORAGE_ERROR")
            .message("File storage operation failed")
            .details(ex.getMessage())
            .build();

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(
            apiResponseFactory.error(
                "File storage operation failed",
                request.getRequestURI(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                error));
  }

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ResponseEntity<ApiResponse<Void>> handleMaxUploadSize(
      MaxUploadSizeExceededException ex, HttpServletRequest request) {

    long maxMB = DataSize.ofBytes(fileProperties.getMaxSize()).toMegabytes();

    ErrorDetails error = ErrorDetails.builder()
        .code("FILE_SIZE_EXCEEDED")
        .message("File size exceeds maximum allowed limit of " + maxMB + " MB")
        .build();

    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
        .body(apiResponseFactory.error(
            "File too large (max " + maxMB + " MB allowed)",
            request.getRequestURI(),
            HttpStatus.PAYLOAD_TOO_LARGE,
            error));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<Void>> handleGeneral(
      Exception ex, HttpServletRequest request) {
    log.error("Unexpected error occurred: {}", ex.getMessage(), ex);

    ErrorDetails error =
        ErrorDetails.builder()
            .code("INTERNAL_SERVER_ERROR")
            .message("An unexpected error occurred")
            .build();

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(
            apiResponseFactory.error(
                "Internal server error",
                request.getRequestURI(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                error));
  }

  @ExceptionHandler(InvalidDateRangeException.class)
  public ResponseEntity<ApiResponse<Void>> handleInvalidDateRange(
      InvalidDateRangeException ex, HttpServletRequest request) {
    log.warn("Invalid date range: {}", ex.getMessage());

    ErrorDetails error =
        ErrorDetails.builder()
            .code("INVALID_DATE_RANGE")
            .message(ex.getMessage())
            .build();

    return ResponseEntity.badRequest()
        .body(
            apiResponseFactory.error(
                ex.getMessage(),
                request.getRequestURI(),
                HttpStatus.BAD_REQUEST,
                error));
  }
}
