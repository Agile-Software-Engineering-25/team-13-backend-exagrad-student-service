package com.ase.exagrad.studentservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import com.ase.exagrad.studentservice.components.ApiResponseFactory;
import com.ase.exagrad.studentservice.dtos.response.ApiResponse;
import com.ase.exagrad.studentservice.dtos.response.ErrorDetails;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    private final ApiResponseFactory apiResponseFactory;

    public GlobalExceptionHandler(ApiResponseFactory apiResponseFactory) {
        this.apiResponseFactory = apiResponseFactory;
    }

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
        log.warn("File size limit exceeded: {}", ex.getMessage());

        ErrorDetails error =
            ErrorDetails.builder()
                .code("FILE_SIZE_EXCEEDED")
                .message("File size exceeds maximum allowed limit")
                .build();

        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
            .body(
                apiResponseFactory.error(
                    "File too large",
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
}
