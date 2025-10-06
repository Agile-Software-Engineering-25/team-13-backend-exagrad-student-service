package com.ase.exagrad.studentservice.service;

import java.io.IOException;
import java.time.Year;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.ase.exagrad.studentservice.config.StorageProperties;
import com.ase.exagrad.studentservice.dto.request.PubDocumentRequest;
import com.ase.exagrad.studentservice.dto.response.PubDocumentResponse;
import com.ase.exagrad.studentservice.entity.PubDocument;
import com.ase.exagrad.studentservice.exception.InvalidDateRangeException;
import com.ase.exagrad.studentservice.mappers.PubDocumentMapper;
import com.ase.exagrad.studentservice.repository.PubDocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PubDocumentService {

  private final PubDocumentRepository pubDocumentRepository;
  private final MinioService minioService;
  private final StorageProperties storageProperties;
  private final FileValidationService fileValidationService;
  private final PubDocumentMapper pubDocumentMapper;

  @Transactional
  public PubDocumentResponse uploadPubDocument(MultipartFile file, PubDocumentRequest metadata)
      throws IOException {
    // Validate file before processing
    fileValidationService.validateFile(file);

    validateDateRange(metadata);
    checkForOverlappingPeriods(metadata);

    String bucketName = storageProperties.getBucketName();
    String sanitizedFilename =
        fileValidationService.sanitizeFileName(file.getOriginalFilename());
    String minioKey = generateMinioKey(sanitizedFilename);

    minioService.uploadFile(
        bucketName, minioKey, file.getInputStream(), file.getSize(), file.getContentType());

    PubDocument doc =
        PubDocument.builder()
            .studentId(metadata.getStudentId())
            .startDate(metadata.getStartDate())
            .endDate(metadata.getEndDate())
            .minioKey(minioKey)
            .fileName(sanitizedFilename)
            .build();

    PubDocument saved = pubDocumentRepository.saveAndFlush(doc);

    String downloadUrl = minioService.getFileUrl(bucketName, saved.getMinioKey());
    return pubDocumentMapper.toResponse(saved, downloadUrl);
  }

  public List<PubDocumentResponse> getDocumentsByStudentId(String studentId) {
    List<PubDocument> documents = pubDocumentRepository.findByStudentId(studentId);
    return convertToResponseWithUrls(documents);
  }

  private List<PubDocumentResponse> convertToResponseWithUrls(List<PubDocument> documents) {
    String bucketName = storageProperties.getBucketName();

    return documents.stream()
        .map(
            doc -> {
              String downloadUrl =
                  minioService.getFileUrl(bucketName, doc.getMinioKey());
              return pubDocumentMapper.toResponse(doc, downloadUrl);
            })
        .toList();
  }

  private String generateMinioKey(String originalFilename) {
    String year = String.valueOf(Year.now().getValue());
    String unique = UUID.randomUUID().toString();
    return "pub-documents/" + year + "/" + unique + "-" + originalFilename;
  }

  private void validateDateRange(PubDocumentRequest metadata) {
    if (metadata.getStartDate()!=null
        && metadata.getEndDate()!=null
        && metadata.getStartDate().isAfter(metadata.getEndDate())) {
      throw new InvalidDateRangeException("The start date must not be after the end date.");
    }
  }

  private void checkForOverlappingPeriods(PubDocumentRequest metadata) {
    if (metadata.getStartDate() == null || metadata.getEndDate() == null) {
      return;
    }

    List<PubDocument> existingDocs = pubDocumentRepository.findByStudentId(metadata.getStudentId());

    boolean overlaps = existingDocs.stream().anyMatch(doc -> {
      if (doc.getStartDate() == null || doc.getEndDate() == null) {
        return false;
      }
      return !metadata.getStartDate().isAfter(doc.getEndDate())
          && !metadata.getEndDate().isBefore(doc.getStartDate());
    });

    if (overlaps) {
      throw new InvalidDateRangeException("Document dates conflict with an existing entry.");
    }
  }
}
