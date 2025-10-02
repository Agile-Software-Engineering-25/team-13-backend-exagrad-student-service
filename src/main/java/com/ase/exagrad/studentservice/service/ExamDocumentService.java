package com.ase.exagrad.studentservice.service;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.Year;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.ase.exagrad.studentservice.config.StorageProperties;
import com.ase.exagrad.studentservice.dto.request.ExamDocumentRequest;
import com.ase.exagrad.studentservice.dto.response.ExamDocumentResponse;
import com.ase.exagrad.studentservice.entity.ExamDocument;
import com.ase.exagrad.studentservice.mappers.ExamDocumentMapper;
import com.ase.exagrad.studentservice.repository.ExamDocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExamDocumentService {

  private final ExamDocumentRepository examDocumentRepository;
  private final MinioService minioService;
  private final StorageProperties storageProperties;
  private final FileValidationService fileValidationService;
  private final ExamDocumentMapper examDocumentMapper;

  @Transactional
  public ExamDocumentResponse uploadExamDocument(MultipartFile file, ExamDocumentRequest metadata)
      throws IOException {
    // Validate file before processing
    fileValidationService.validateFile(file);

    String bucketName = storageProperties.getExamDocumentsBucket();
    String sanitizedFilename =
        fileValidationService.sanitizeFileName(file.getOriginalFilename());
    String minioKey = generateMinioKey(sanitizedFilename);

    minioService.uploadFile(
        bucketName, minioKey, file.getInputStream(), file.getSize(), file.getContentType());

    ExamDocument doc =
        ExamDocument.builder()
            .examId(metadata.getExamId())
            .studentId(metadata.getStudentId())
            .minioKey(minioKey)
            .fileName(sanitizedFilename)
            .build();

    ExamDocument saved = examDocumentRepository.saveAndFlush(doc);

    String downloadUrl = minioService.getFileUrl(bucketName, saved.getMinioKey());
    return examDocumentMapper.toResponse(saved, downloadUrl);
  }

  public List<ExamDocumentResponse> getDocumentsByStudentId(String studentId) {
    List<ExamDocument> documents = examDocumentRepository.findByStudentId(studentId);
    return convertToResponseWithUrls(documents);
  }

  public List<ExamDocumentResponse> getDocumentsByExamId(String examId) {
    List<ExamDocument> documents = examDocumentRepository.findByExamId(examId);
    return convertToResponseWithUrls(documents);
  }

  private List<ExamDocumentResponse> convertToResponseWithUrls(List<ExamDocument> documents) {
    String bucketName = storageProperties.getExamDocumentsBucket();

    return documents.stream()
        .map(
            doc -> {
              String downloadUrl =
                  minioService.getFileUrl(bucketName, doc.getMinioKey());
              return examDocumentMapper.toResponse(doc, downloadUrl);
            })
        .toList();
  }

  @Transactional
  public void deleteExamDocument(String documentId) {
    UUID uuid;
    try {
      uuid = UUID.fromString(documentId);
    }
    catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Invalid document ID format");
    }

    ExamDocument document =
        examDocumentRepository
            .findById(uuid)
            .orElseThrow(() -> new IllegalArgumentException("Document not found"));

    // Mock deadline check - TODO: Replace with actual Team 14 API call
    Instant deadline = getMockDeadlineForExam(document.getExamId());
    if (Instant.now().isAfter(deadline)) {
      throw new IllegalStateException(
          "Deletion not allowed: deadline has passed for this exam");
    }

    // Delete from MinIO
    String bucketName = storageProperties.getExamDocumentsBucket();
    minioService.deleteFile(bucketName, document.getMinioKey());

    // Delete from database
    examDocumentRepository.delete(document);

    log.info("Deleted exam document with ID: {}", documentId);
  }

  /**
   * Mock method to get deadline for an exam.
   * TODO: Replace with actual call to Team 14 API
   * For now, returns a deadline 7 days after document upload
   */
  private Instant getMockDeadlineForExam(String examId) {
    final long DEADLINE_DAYS = 7L;
    return Instant.now().plus(Duration.ofDays(DEADLINE_DAYS));
  }

  private String generateMinioKey(String originalFilename) {
    String year = String.valueOf(Year.now().getValue());
    String unique = UUID.randomUUID().toString();
    return "exam-documents/" + year + "/" + unique + "-" + originalFilename;
  }
}
