package com.ase.exagrad.studentservice.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

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
