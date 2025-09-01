package com.ase.exagrad.studentservice.repositories;

import com.ase.exagrad.studentservice.entities.ExamDocument;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamDocumentRepository extends JpaRepository<ExamDocument, UUID> {
}
