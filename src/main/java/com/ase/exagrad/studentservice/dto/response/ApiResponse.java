package com.ase.exagrad.studentservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.http.HttpStatus;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private int statusCode;
    private String status;
    private String message;
    private Instant timestamp;
    private String endpoint;

    private T data;

    private ErrorDetails error;

    public static <T> ApiResponse<T> success(T data, String endpoint, HttpStatus httpStatus) {
        return ApiResponse.<T>builder()
                .success(true)
                .statusCode(httpStatus.value())
                .status(httpStatus.getReasonPhrase())
                .data(data)
                .timestamp(Instant.now())
                .endpoint(endpoint)
                .build();
    }

    public static <T> ApiResponse<T> success(T data, String endpoint) {
        return success(data, endpoint, HttpStatus.OK);
    }

    public static <T> ApiResponse<T> created(T data, String endpoint) {
        return success(data, endpoint, HttpStatus.CREATED);
    }

    public static <T> ApiResponse<T> error(
            String message, String endpoint, HttpStatus httpStatus, ErrorDetails errorDetails) {
        return ApiResponse.<T>builder()
                .success(false)
                .statusCode(httpStatus.value())
                .status(httpStatus.getReasonPhrase())
                .message(message)
                .error(errorDetails)
                .timestamp(Instant.now())
                .endpoint(endpoint)
                .build();
    }

    public static <T> ApiResponse<T> badRequest(String message, String endpoint) {
        return error(
                message,
                endpoint,
                HttpStatus.BAD_REQUEST,
                ErrorDetails.builder().code("VALIDATION_ERROR").message(message).build());
    }

    public static <T> ApiResponse<T> internalServerError(String message, String endpoint) {
        return error(
                message,
                endpoint,
                HttpStatus.INTERNAL_SERVER_ERROR,
                ErrorDetails.builder().code("INTERNAL_ERROR").message(message).build());
    }
}
