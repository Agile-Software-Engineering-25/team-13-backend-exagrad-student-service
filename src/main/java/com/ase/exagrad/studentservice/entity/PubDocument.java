package com.ase.exagrad.studentservice.entity;

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
import org.hibernate.annotations.CreationTimestamp;
import java.time.Instant;
import java.util.UUID;

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
  private String pubId;

  @Column(nullable = false)
  private String studentId;

  @Column //nullable?
  private String startDate;

  @Column // nullable?
  private String endDate;

  //Column for exams affected?

  @CreationTimestamp
  @Column(nullable = false, updatable = false) //updatable false needed?
  private Instant uploadDate;

  @Column(nullable = false, unique = true)
  private String minioKey;

  @Column(nullable = false)
  private String fileName;
}
