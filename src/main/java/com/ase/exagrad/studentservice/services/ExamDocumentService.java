package com.ase.exagrad.studentservice.services;

import com.ase.exagrad.studentservice.entities.ExamDocument;
import com.ase.exagrad.studentservice.repositories.ExamDocumentRepository;
import com.ase.exagrad.studentservice.dto.ExamDocumentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Year;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExamDocumentService {

  private final ExamDocumentRepository examDocumentRepository;
  private final MinioService minioService; // <-- dein MinIO-Wrapper

  // TODO: load from application.yml
  private static final String BUCKET_NAME = "team13-student-exam-documents";

  @Transactional
  public ExamDocument uploadExamDocument(MultipartFile file, ExamDocumentRequest metadata) throws IOException {
    // 1. MinIO Key generieren
    String minioKey = generateMinioKey(file.getOriginalFilename());

    // 2. Datei nach MinIO hochladen
    minioService.uploadFile(BUCKET_NAME, minioKey, file.getInputStream(), file.getSize(), file.getContentType());

    // 3. Metadaten speichern
    ExamDocument doc = ExamDocument.builder()
        .title(metadata.getTitle())
        .examId(metadata.getExamId())
        .studentId(metadata.getStudentId() != null ? metadata.getStudentId() : "DEFAULT-STUDENT")
        .minioKey(minioKey)
        .build();

    return examDocumentRepository.save(doc);
  }

  public List<ExamDocument> getDocumentsByStudentId(String studentId) {
    // TODO: replace with query in repository -> more efficient that filter after load all
    return examDocumentRepository.findAll()
        .stream()
        .filter(doc -> doc.getStudentId().equals(studentId))
        .toList();
  }

  private String generateMinioKey(String originalFilename) {
    String year = String.valueOf(Year.now().getValue());
    String unique = UUID.randomUUID().toString();
    return "exam-documents/" + year + "/" + unique + "-" + originalFilename;
  }
}
