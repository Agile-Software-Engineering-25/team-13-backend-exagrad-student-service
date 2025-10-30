package com.ase.exagrad.studentservice.controller;

import com.ase.exagrad.studentservice.dto.ExamDataDto;
import com.ase.exagrad.studentservice.service.ExamDataApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/data")
public class ExamDataController {
  private final ExamDataApiService examDataApiService;

  public ExamDataController(ExamDataApiService examDataApiService) {
    this.examDataApiService = examDataApiService;
  }

  @GetMapping
  @RequestMapping("/exams")
  @Operation(
      summary = "Get exam data",
      description = "Get all exam data")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Exam Data retrieved successfully"),
  })
  public ResponseEntity<List<ExamDataDto>> getAllExamData(){
    List<ExamDataDto> examData = examDataApiService.fetchAllExamData();

    return ResponseEntity.ok(examData);
  }

}
