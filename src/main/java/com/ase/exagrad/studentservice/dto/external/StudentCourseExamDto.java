package com.ase.exagrad.studentservice.dto.external;

import com.ase.exagrad.studentservice.dto.ExamDataDto;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentCourseExamDto {

  private String courseName;
  private String courseCode;
  private String lecturer = "Test Doz";
  private Integer semester;
  private Integer creditPoints;
  private List<ExamDataDto> exams;
}

