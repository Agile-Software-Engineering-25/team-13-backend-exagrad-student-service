package com.ase.exagrad.studentservice.component;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import com.ase.exagrad.studentservice.dto.response.ApiResponse;
import com.ase.exagrad.studentservice.dto.response.ErrorDetails;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ApiResponseFactory {

  private final TimeProvider timeProvider;

  public <T> ApiResponse<T> success(T data, String endpoint, HttpStatus httpStatus) {
    return ApiResponse.<T>builder()
        .success(true)
        .statusCode(httpStatus.value())
        .status(httpStatus.getReasonPhrase())
        .data(data)
        .timestamp(timeProvider.now())
        .endpoint(endpoint)
        .build();
  }

  public <T> ApiResponse<T> success(T data, String endpoint) {
    return success(data, endpoint, HttpStatus.OK);
  }

  public <T> ApiResponse<T> created(T data, String endpoint) {
    return success(data, endpoint, HttpStatus.CREATED);
  }

  public <T> ApiResponse<T> error(
      String message, String endpoint, HttpStatus httpStatus, ErrorDetails errorDetails) {
    return ApiResponse.<T>builder()
        .success(false)
        .statusCode(httpStatus.value())
        .status(httpStatus.getReasonPhrase())
        .message(message)
        .error(errorDetails)
        .timestamp(timeProvider.now())
        .endpoint(endpoint)
        .build();
  }

  public <T> ApiResponse<T> badRequest(String message, String endpoint) {
    return error(
        message,
        endpoint,
        HttpStatus.BAD_REQUEST,
        ErrorDetails.builder().code("VALIDATION_ERROR").message(message).build());
  }

  public <T> ApiResponse<T> internalServerError(String message, String endpoint) {
    return error(
        message,
        endpoint,
        HttpStatus.INTERNAL_SERVER_ERROR,
        ErrorDetails.builder().code("INTERNAL_ERROR").message(message).build());
  }
}
