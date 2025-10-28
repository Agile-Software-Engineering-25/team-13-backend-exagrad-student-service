package com.ase.exagrad.studentservice.component;

import com.ase.exagrad.studentservice.dto.response.ApiResponseWrapper;
import com.ase.exagrad.studentservice.dto.response.ErrorDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApiResponseFactory {

  private final TimeProvider timeProvider;

  public <T> ApiResponseWrapper<T> success(T data, String endpoint, HttpStatus httpStatus) {
    return ApiResponseWrapper.<T>builder()
        .success(true)
        .statusCode(httpStatus.value())
        .status(httpStatus.getReasonPhrase())
        .data(data)
        .timestamp(timeProvider.now())
        .endpoint(endpoint)
        .build();
  }

  public <T> ApiResponseWrapper<T> success(T data, String endpoint) {
    return success(data, endpoint, HttpStatus.OK);
  }

  public <T> ApiResponseWrapper<T> created(T data, String endpoint) {
    return success(data, endpoint, HttpStatus.CREATED);
  }

  public <T> ApiResponseWrapper<T> error(
      String message, String endpoint, HttpStatus httpStatus, ErrorDetails errorDetails) {
    return ApiResponseWrapper.<T>builder()
        .success(false)
        .statusCode(httpStatus.value())
        .status(httpStatus.getReasonPhrase())
        .message(message)
        .error(errorDetails)
        .timestamp(timeProvider.now())
        .endpoint(endpoint)
        .build();
  }

  public <T> ApiResponseWrapper<T> badRequest(String message, String endpoint) {
    return error(
        message,
        endpoint,
        HttpStatus.BAD_REQUEST,
        ErrorDetails.builder().code("VALIDATION_ERROR").message(message).build());
  }

  public <T> ApiResponseWrapper<T> internalServerError(String message, String endpoint) {
    return error(
        message,
        endpoint,
        HttpStatus.INTERNAL_SERVER_ERROR,
        ErrorDetails.builder().code("INTERNAL_ERROR").message(message).build());
  }
}
