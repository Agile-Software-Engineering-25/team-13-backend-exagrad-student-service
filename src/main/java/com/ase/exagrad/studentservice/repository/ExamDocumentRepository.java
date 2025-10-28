package com.ase.exagrad.studentservice.repository;

import com.ase.exagrad.studentservice.entity.ExamDocument;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamDocumentRepository extends JpaRepository<ExamDocument, UUID> {
  List<ExamDocument> findByStudentId(String studentId);

  List<ExamDocument> findByExamId(String examId);
}
