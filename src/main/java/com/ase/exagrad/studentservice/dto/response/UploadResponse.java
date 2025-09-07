package com.ase.exagrad.studentservice.dto.response;

import com.ase.exagrad.studentservice.entities.ExamDocument;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadResponse {

    private UUID id;
    private String fileName;

    public static UploadResponse fromEntity(ExamDocument entity) {
        return UploadResponse.builder().id(entity.getId()).fileName(entity.getFileName()).build();
    }
}
