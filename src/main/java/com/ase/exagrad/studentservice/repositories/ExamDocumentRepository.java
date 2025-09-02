package com.ase.exagrad.studentservice.repositories;

import com.ase.exagrad.studentservice.entities.ExamDocument;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ExamDocumentRepository extends JpaRepository<ExamDocument, UUID> {
    List<ExamDocument> findByStudentId(String studentId);

    List<ExamDocument> findByExamId(String examID);
}
