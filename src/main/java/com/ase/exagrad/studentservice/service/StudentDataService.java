package com.ase.exagrad.studentservice.service;

import com.ase.exagrad.studentservice.dto.StudentDataDto;
import com.ase.exagrad.studentservice.dto.external.StudentCourseExamDto;
import com.ase.exagrad.studentservice.dto.external.StudentDto;
import com.ase.exagrad.studentservice.service.external.CourseExamService;
import com.ase.exagrad.studentservice.service.external.StudentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentDataService {

  private final StudentService studentService;
  private final CourseExamService courseExamService;

  public StudentDataDto getStudentData(String studentId, String authorizationHeader) {
    StudentDto studentDto = studentService.fetchDataForStudent(studentId, authorizationHeader);
    List<StudentCourseExamDto> courses =
        courseExamService.fetchCoursesWithExamsForStudent(studentId);

    return StudentDataDto.builder().student(studentDto).courses(courses).build();
  }
}
