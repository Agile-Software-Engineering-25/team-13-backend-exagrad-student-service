package com.ase.exagrad.studentservice.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private int statusCode;
    private String status;
    private String message;
    private ZonedDateTime timestamp;
    private String endpoint;
    private T data;
    private ErrorDetails error;
}
