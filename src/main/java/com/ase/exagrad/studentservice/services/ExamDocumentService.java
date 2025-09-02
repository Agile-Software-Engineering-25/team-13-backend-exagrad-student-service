package com.ase.exagrad.studentservice.services;

import com.ase.exagrad.studentservice.config.MinioProperties;
import com.ase.exagrad.studentservice.dto.ExamDocumentRequest;
import com.ase.exagrad.studentservice.dto.ExamDocumentResponse;
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
import java.util.stream.Collectors;

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
                        .fileName(file.getOriginalFilename()) // Store clean filename
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
        String bucketName = minioProperties.getBuckets().get("examDocuments");

        return documents.stream()
                .map(
                        doc -> {
                            String downloadUrl =
                                    minioService.getFileUrl(bucketName, doc.getMinioKey());
                            return ExamDocumentResponse.builder()
                                    .examId(doc.getExamId())
                                    .studentId(doc.getStudentId())
                                    .uploadDate(doc.getUploadDate())
                                    .downloadUrl(downloadUrl)
                                    .fileName(doc.getFileName())
                                    .build();
                        })
                .collect(Collectors.toList());
    }

    private String generateMinioKey(String originalFilename) {
        String year = String.valueOf(Year.now().getValue());
        String unique = UUID.randomUUID().toString();
        return "exam-documents/" + year + "/" + unique + "-" + originalFilename;
    }
}
