package com.ase.exagrad.studentservice.dto.external;

import com.ase.exagrad.studentservice.dto.ExamDataDto;
import java.util.List;
import lombok.Data;

@Data
public class StudentCourseExamDto {

  private Long courseId;
  private String courseName;
  private String courseCode;
  private Integer semester;
  private String examType;
  private Integer creditPoints;
  private Integer totalUnits;
  private Boolean elective;
  private List<ExamDataDto> exams;
}
