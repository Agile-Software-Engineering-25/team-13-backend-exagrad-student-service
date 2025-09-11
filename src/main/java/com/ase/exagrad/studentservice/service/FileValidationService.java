package com.ase.exagrad.studentservice.service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.ase.exagrad.studentservice.config.FileProperties;
import com.ase.exagrad.studentservice.exception.FileValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileValidationService {

  private static final Set<String> DANGEROUS_EXTENSIONS =
      Set.of("exe", "bat", "cmd", "com", "scr", "vbs", "js", "jar", "sh");
  private final FileProperties fileProperties;

  public void validateFile(MultipartFile file) {
    if (file == null || file.isEmpty()) {
      throw new FileValidationException("File is required and cannot be empty");
    }

    validateFileName(file.getOriginalFilename());
    validateFileSize(file.getSize());
    validateContentType(file.getContentType());
  }

  private void validateFileName(String fileName) {
    if (fileName == null || fileName.trim().isEmpty()) {
      throw new FileValidationException("File name is required");
    }

    // Sanitize filename - remove path traversal attempts
    String sanitized = fileName.replaceAll("[\\\\/:*?\"<>|]", "_");
    if (!sanitized.equals(fileName)) {
      log.warn(
          "Filename contained illegal characters, sanitized: {} -> {}",
          fileName,
          sanitized);
    }

    // Check for dangerous extensions
    String extension = getFileExtension(fileName).toLowerCase();
    if (DANGEROUS_EXTENSIONS.contains(extension)) {
      throw new FileValidationException("File type not allowed: " + extension);
    }

    // Filename length check
    final int maxFilenameLength = 255;
    if (fileName.length() > maxFilenameLength) {
      throw new FileValidationException(
          "File name is too long (max " + maxFilenameLength + " characters)");
    }
  }

  private void validateFileSize(long size) {
    long maxFileSize = fileProperties.getMaxSize();
    if (size > maxFileSize) {
      throw new FileValidationException(
          String.format(
              "File size exceeds maximum allowed size of %d bytes", maxFileSize));
    }

    if (size == 0) {
      throw new FileValidationException("File cannot be empty");
    }
  }

  private void validateContentType(String contentType) {
    if (contentType == null) {
      throw new FileValidationException("Content type is required");
    }

    List<String> allowedTypes = Arrays.asList(fileProperties.getAllowedTypes().split(","));
    if (!allowedTypes.contains(contentType.trim())) {
      throw new FileValidationException("Content type not allowed: " + contentType);
    }
  }

  private boolean startsWithSignature(byte[] data, byte[] signature) {
    if (data.length < signature.length) {
      return false;
    }

    for (int i = 0; i < signature.length; i++) {
      if (data[i] != signature[i]) {
        return false;
      }
    }
    return true;
  }

  private String getFileExtension(String fileName) {
    int lastDotIndex = fileName.lastIndexOf('.');
    return lastDotIndex > 0 ? fileName.substring(lastDotIndex + 1) : "";
  }

  public String sanitizeFileName(String fileName) {
    if (fileName == null) {
      return "unnamed_file";
    }
    return fileName.replaceAll("[\\\\/:*?\"<>|]", "_").replaceAll("\\s+", "_").trim();
  }
}
