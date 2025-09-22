package com.ase.exagrad.studentservice.entity;

import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "exam_documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamDocument {

  @Id
  @GeneratedValue
  private UUID id;

  @Column(nullable = false)
  private String examId;

  @Column(nullable = false)
  private String studentId;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private Instant uploadDate;

  @Column(nullable = false, unique = true)
  private String minioKey;

  @Column(nullable = false)
  private String fileName;
}
