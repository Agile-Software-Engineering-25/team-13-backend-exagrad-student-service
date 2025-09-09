package com.ase.exagrad.studentservice.mappers;

import java.time.ZoneId;
import org.springframework.stereotype.Component;
import com.ase.exagrad.studentservice.dtos.response.ExamDocumentResponse;
import com.ase.exagrad.studentservice.entities.ExamDocument;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExamDocumentMapper {

  private final ZoneId appZoneId;

  public ExamDocumentResponse toResponse(ExamDocument entity, String downloadUrl) {
    return ExamDocumentResponse.builder()
        .id(entity.getId())
        .examId(entity.getExamId())
        .studentId(entity.getStudentId())
        .uploadDate(entity.getUploadDate().atZone(appZoneId))
        .downloadUrl(downloadUrl)
        .fileName(entity.getFileName())
        .build();
  }
}
