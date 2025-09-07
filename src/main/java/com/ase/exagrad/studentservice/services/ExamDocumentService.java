package com.ase.exagrad.studentservice.services;

import com.ase.exagrad.studentservice.config.StorageProperties;
import com.ase.exagrad.studentservice.dto.request.ExamDocumentRequest;
import com.ase.exagrad.studentservice.dto.response.ExamDocumentResponse;
import com.ase.exagrad.studentservice.entities.ExamDocument;
import com.ase.exagrad.studentservice.repositories.ExamDocumentRepository;

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
    private final MinioService minioService;
    private final StorageProperties storageProperties;
    private final FileValidationService fileValidationService;

    @Transactional
    public ExamDocument uploadExamDocument(MultipartFile file, ExamDocumentRequest metadata)
            throws IOException {
        // Validate file before processing
        fileValidationService.validateFile(file);
        
        String bucketName = storageProperties.getExamDocumentsBucket();
        String sanitizedFilename = fileValidationService.sanitizeFileName(file.getOriginalFilename());
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

        return examDocumentRepository.save(doc);
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
                            return ExamDocumentResponse.fromEntity(doc, downloadUrl);
                        })
                .toList();
    }

    private String generateMinioKey(String originalFilename) {
        String year = String.valueOf(Year.now().getValue());
        String unique = UUID.randomUUID().toString();
        return "exam-documents/" + year + "/" + unique + "-" + originalFilename;
    }
}
