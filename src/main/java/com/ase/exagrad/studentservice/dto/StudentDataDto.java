package com.ase.exagrad.studentservice.dto;

import com.ase.exagrad.studentservice.dto.external.StudentCourseExamDto;
import com.ase.exagrad.studentservice.dto.external.StudentDto;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentDataDto {

  private StudentDto student;
  private List<StudentCourseExamDto> courses;
}
