package com.ase.exagrad.studentservice.exception;

public class FileValidationException extends RuntimeException {

  public FileValidationException(String message) {
    super(message);
  }

  public FileValidationException(String message, Throwable cause) {
    super(message, cause);
  }
}
