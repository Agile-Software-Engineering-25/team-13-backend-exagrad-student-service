package com.ase.exagrad.studentservice.mappers;

import java.time.ZoneId;
import org.springframework.stereotype.Component;
import com.ase.exagrad.studentservice.dto.response.PubDocumentResponse;
import com.ase.exagrad.studentservice.entity.PubDocument;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PubDocumentMapper {

  private final ZoneId appZoneId;

  public PubDocumentResponse toResponse(PubDocument entity, String downloadUrl) {
    return PubDocumentResponse.builder()
        .id(entity.getId())
        .pubId(entity.getPubId())
        .studentId(entity.getStudentId())
        .uploadDate(entity.getUploadDate().atZone(appZoneId))
        .startDate(entity.getStartDate())
        .endDate(entity.getEndDate())
        .downloadUrl(downloadUrl)
        .fileName(entity.getFileName())
        .build();
  }
}
