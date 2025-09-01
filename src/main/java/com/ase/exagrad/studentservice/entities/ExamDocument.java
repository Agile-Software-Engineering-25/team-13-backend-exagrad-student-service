package com.ase.exagrad.studentservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

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
  private String title;

  /**
   * Refers to the exam this document belongs to.
   */
  @Column(nullable = false)
  private String examId;

  /**
   * Refers to the student/user this document belongs to.
   * TODO: maybe we have to distinguish later between 'user belongs to' and 'user uploaded'
   */
  @Column(nullable = false)
  private String studentId;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private Instant uploadDate;

  @Column(nullable = false, unique = true)
  private String minioKey;
}
