package com.ase.exagrad.studentservice.repositories;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ase.exagrad.studentservice.entities.ExamDocument;

public interface ExamDocumentRepository extends JpaRepository<ExamDocument, UUID> {
    List<ExamDocument> findByStudentId(String studentId);

    List<ExamDocument> findByExamId(String examId);
}
