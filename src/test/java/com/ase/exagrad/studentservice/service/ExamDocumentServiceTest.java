package com.ase.exagrad.studentservice.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.ase.exagrad.studentservice.config.StorageProperties;
import com.ase.exagrad.studentservice.entity.ExamDocument;
import com.ase.exagrad.studentservice.mappers.ExamDocumentMapper;
import com.ase.exagrad.studentservice.repository.ExamDocumentRepository;

@ExtendWith(MockitoExtension.class)
class ExamDocumentServiceTest {

  @Mock
  private ExamDocumentRepository examDocumentRepository;

  @Mock
  private MinioService minioService;

  @Mock
  private StorageProperties storageProperties;

  @Mock
  private FileValidationService fileValidationService;

  @Mock
  private ExamDocumentMapper examDocumentMapper;

  @InjectMocks
  private ExamDocumentService examDocumentService;

  private UUID testDocumentId;
  private ExamDocument testDocument;
  private String bucketName;

  @BeforeEach
  void setUp() {
    testDocumentId = UUID.randomUUID();
    bucketName = "exam-documents-bucket";

    testDocument =
        ExamDocument.builder()
            .id(testDocumentId)
            .examId("EXAM123")
            .studentId("STUDENT123")
            .minioKey("exam-documents/2025/test-file.pdf")
            .fileName("test-file.pdf")
            .build();
  }

  @Test
  void deleteExamDocumentValidIdBeforeDeadlineSucceeds() {
    // Arrange
    when(examDocumentRepository.findById(testDocumentId)).thenReturn(Optional.of(testDocument));
    when(storageProperties.getExamDocumentsBucket()).thenReturn(bucketName);

    // Act
    assertDoesNotThrow(() -> examDocumentService.deleteExamDocument(testDocumentId.toString()));

    // Assert
    verify(examDocumentRepository, times(1)).findById(testDocumentId);
    verify(minioService, times(1)).deleteFile(bucketName, testDocument.getMinioKey());
    verify(examDocumentRepository, times(1)).delete(testDocument);
  }

  @Test
  void deleteExamDocumentInvalidUuidFormatThrowsIllegalArgumentException() {
    // Arrange
    String invalidId = "not-a-uuid";

    // Act & Assert
    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> examDocumentService.deleteExamDocument(invalidId));

    assertEquals("Invalid document ID format", exception.getMessage());
    verify(examDocumentRepository, never()).findById(any());
    verify(minioService, never()).deleteFile(anyString(), anyString());
  }

  @Test
  void deleteExamDocumentNonExistentIdThrowsIllegalArgumentException() {
    // Arrange
    UUID nonExistentId = UUID.randomUUID();
    when(examDocumentRepository.findById(nonExistentId)).thenReturn(Optional.empty());

    // Act & Assert
    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> examDocumentService.deleteExamDocument(nonExistentId.toString()));

    assertEquals("Document not found", exception.getMessage());
    verify(examDocumentRepository, times(1)).findById(nonExistentId);
    verify(minioService, never()).deleteFile(anyString(), anyString());
    verify(examDocumentRepository, never()).delete(any());
  }

  @Test
  void deleteExamDocumentMinioServiceDeletesFileSuccessfully() {
    // Arrange
    when(examDocumentRepository.findById(testDocumentId)).thenReturn(Optional.of(testDocument));
    when(storageProperties.getExamDocumentsBucket()).thenReturn(bucketName);

    // Act
    examDocumentService.deleteExamDocument(testDocumentId.toString());

    // Assert
    verify(minioService, times(1))
        .deleteFile(eq(bucketName), eq(testDocument.getMinioKey()));
  }

  @Test
  void deleteExamDocumentDatabaseRecordDeletedAfterMinioSuccess() {
    // Arrange
    when(examDocumentRepository.findById(testDocumentId)).thenReturn(Optional.of(testDocument));
    when(storageProperties.getExamDocumentsBucket()).thenReturn(bucketName);

    // Act
    examDocumentService.deleteExamDocument(testDocumentId.toString());

    // Assert
    verify(examDocumentRepository, times(1)).delete(testDocument);
  }
}
