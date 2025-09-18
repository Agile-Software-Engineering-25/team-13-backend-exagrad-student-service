package com.ase.exagrad.studentservice.repository;

import com.ase.exagrad.studentservice.entity.PubDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

//wofür ist repository nochmal? fehlt was?
public interface PubDocumentRepository extends JpaRepository<PubDocument, UUID> {
  List<PubDocument> findByStudentId(String studentId);

  List<PubDocument> findByPubId(String pubId);
}
