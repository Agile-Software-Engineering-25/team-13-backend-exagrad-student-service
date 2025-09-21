package com.ase.exagrad.studentservice.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ase.exagrad.studentservice.entity.PubDocument;

public interface PubDocumentRepository extends JpaRepository<PubDocument, UUID> {
  List<PubDocument> findByStudentId(String studentId);
}
