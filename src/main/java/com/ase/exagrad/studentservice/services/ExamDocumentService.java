package com.ase.exagrad.studentservice.services;

import com.ase.exagrad.studentservice.config.MinioProperties;
import com.ase.exagrad.studentservice.dto.ExamDocumentRequest;
import com.ase.exagrad.studentservice.entities.ExamDocument;
import com.ase.exagrad.studentservice.repositories.ExamDocumentRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Year;
import java.util.List;
import java.util.UUID;

@Service
@Component
@RequiredArgsConstructor
public class ExamDocumentService {

    private final ExamDocumentRepository examDocumentRepository;
    private final MinioService minioService;
    private final MinioProperties minioProperties;

    @Transactional
    public ExamDocument uploadExamDocument(MultipartFile file, ExamDocumentRequest metadata)
            throws IOException {
        String bucketName = minioProperties.getBuckets().get("examDocuments");
        String minioKey = generateMinioKey(file.getOriginalFilename());

        minioService.uploadFile(
                bucketName, minioKey, file.getInputStream(), file.getSize(), file.getContentType());

        ExamDocument doc =
                ExamDocument.builder()
                        .examId(metadata.getExamId())
                        .studentId(
                                metadata.getStudentId() != null
                                        ? metadata.getStudentId()
                                        : "DEFAULT-STUDENT")
                        .minioKey(minioKey)
                        .build();

        return examDocumentRepository.save(doc);
    }

    public List<ExamDocument> getDocumentsByStudentId(String studentId) {
        return examDocumentRepository.findByStudentId(studentId);
    }

    public List<ExamDocument> getDocumentsByExamId(String examId) {
        return examDocumentRepository.findByExamId(examId);
    }

    private String generateMinioKey(String originalFilename) {
        String year = String.valueOf(Year.now().getValue());
        String unique = UUID.randomUUID().toString();
        return "exam-documents/" + year + "/" + unique + "-" + originalFilename;
    }
}
