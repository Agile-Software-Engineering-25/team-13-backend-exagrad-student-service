package com.ase.exagrad.studentservice.controller;

import com.ase.exagrad.studentservice.dto.ExamData;
import com.ase.exagrad.studentservice.service.ExamDataApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
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
  public ResponseEntity<List<ExamData>> getAllExamData(){
    List<ExamData> examData = examDataApiService.fetchAllExamData();

    return ResponseEntity.ok(examData);
  }

  @RequestMapping("/exam/{id}")
  public ResponseEntity<Mono<ExamData>> getOneExamData(@PathVariable String id){
    Mono<ExamData> examData = examDataApiService.fetchOneExamData(id);

    return ResponseEntity.ok(examData);
  }
}
