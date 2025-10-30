package com.ase.exagrad.studentservice.dto.external;

import java.time.LocalDate;
import lombok.Data;

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
