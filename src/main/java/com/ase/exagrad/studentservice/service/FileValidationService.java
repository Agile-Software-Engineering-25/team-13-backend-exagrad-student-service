package com.ase.exagrad.studentservice.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.ase.exagrad.studentservice.exception.FileValidationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FileValidationService {

  private static final Set<String> DANGEROUS_EXTENSIONS =
      Set.of("exe", "bat", "cmd", "com", "scr", "vbs", "js", "jar", "sh");
  // Magic bytes for common file types
  private static final byte[] PDF_SIGNATURE = {0x25, 0x50, 0x44, 0x46}; // %PDF
  private static final byte[] PNG_SIGNATURE = {
      (byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A
  };
  private static final byte[] JPEG_SIGNATURE = {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF};

  @Value("${app.file.max-size:10485760}") // 10MB default
  private long maxFileSize;

  @Value(
      "${app.file.allowed-types:application/pdf,image/jpeg,image/png,"
          + "image/gif,text/plain,application/msword,"
          + "application/vnd.openxmlformats-officedocument.wordprocessingml.document}")
  private String allowedTypesConfig;

  public void validateFile(MultipartFile file) {
    if (file==null || file.isEmpty()) {
      throw new FileValidationException("File is required and cannot be empty");
    }

    validateFileName(file.getOriginalFilename());
    validateFileSize(file.getSize());
    validateContentType(file.getContentType());
    validateFileContent(file);
  }

  private void validateFileName(String fileName) {
    if (fileName==null || fileName.trim().isEmpty()) {
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
    if (size > maxFileSize) {
      throw new FileValidationException(
          String.format(
              "File size exceeds maximum allowed size of %d bytes", maxFileSize));
    }

    if (size==0) {
      throw new FileValidationException("File cannot be empty");
    }
  }

  private void validateContentType(String contentType) {
    if (contentType==null) {
      throw new FileValidationException("Content type is required");
    }

    List<String> allowedTypes = Arrays.asList(allowedTypesConfig.split(","));
    if (!allowedTypes.contains(contentType.trim())) {
      throw new FileValidationException("Content type not allowed: " + contentType);
    }
  }

  private void validateFileContent(MultipartFile file) {
    try (InputStream inputStream = file.getInputStream()) {
      final int headerSize = 10;
      byte[] header = new byte[headerSize];
      int bytesRead = inputStream.read(header);

      final int minBytesRequired = 4;
      if (bytesRead < minBytesRequired) {
        throw new FileValidationException("File content appears to be corrupted");
      }

      // Basic magic number validation for common types
      String contentType = file.getContentType();
      if ("application/pdf".equals(contentType)) {
        if (!startsWithSignature(header, PDF_SIGNATURE)) {
          throw new FileValidationException("File content does not match PDF format");
        }
      }
      else if ("image/png".equals(contentType)) {
        if (!startsWithSignature(header, PNG_SIGNATURE)) {
          throw new FileValidationException("File content does not match PNG format");
        }
      }
      else if ("image/jpeg".equals(contentType)) {
        if (!startsWithSignature(header, JPEG_SIGNATURE)) {
          throw new FileValidationException("File content does not match JPEG format");
        }
      }

    }
    catch (IOException e) {
      throw new FileValidationException("Failed to read file content for validation", e);
    }
  }

  private boolean startsWithSignature(byte[] data, byte[] signature) {
    if (data.length < signature.length) {
      return false;
    }

    for (int i = 0; i < signature.length; i++) {
      if (data[i]!=signature[i]) {
        return false;
      }
    }
    return true;
  }

  private String getFileExtension(String fileName) {
    int lastDotIndex = fileName.lastIndexOf('.');
    return lastDotIndex > 0 ? fileName.substring(lastDotIndex + 1):"";
  }

  public String sanitizeFileName(String fileName) {
    if (fileName==null) {
      return "unnamed_file";
    }
    return fileName.replaceAll("[\\\\/:*?\"<>|]", "_").replaceAll("\\s+", "_").trim();
  }
}
