package com.ase.exagrad.studentservice.repositories;

import com.ase.exagrad.studentservice.entities.ExamDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ExamDocumentRepository  extends JpaRepository<ExamDocument, UUID> {
}
