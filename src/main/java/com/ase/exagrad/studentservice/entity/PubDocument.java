package com.ase.exagrad.studentservice.entity;

import java.time.Instant;
import java.time.LocalDate;
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
@Table(name = "pub_documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PubDocument {

  @Id
  @GeneratedValue
  private UUID id;

  @Column(nullable = false)
  private String studentId;

  @Column(nullable = false)
  private LocalDate startDate;

  @Column(nullable = false)
  private LocalDate endDate;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private Instant uploadDate;

  @Column(nullable = false, unique = true)
  private String minioKey;

  @Column(nullable = false)
  private String fileName;
}
