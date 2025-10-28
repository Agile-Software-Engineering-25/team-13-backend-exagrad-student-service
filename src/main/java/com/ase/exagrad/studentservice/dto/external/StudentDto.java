package com.ase.exagrad.studentservice.dto.external;

import lombok.Data;
import java.time.LocalDate;

@Data
public class StudentDto {
  private String id;
  private LocalDate dateOfBirth;
  private String address;
  private String phoneNumber;
  private String username;
  private String firstName;
  private String lastName;
  private String email;
  private String matriculationNumber;
  private String degreeProgram;
  private int semester;
  private String studyStatus;
  private String cohort;
}
