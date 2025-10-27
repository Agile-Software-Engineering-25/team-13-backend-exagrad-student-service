package com.ase.exagrad.studentservice.service;

import com.ase.exagrad.studentservice.dto.ExamDataDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.List;

@Service
public class ExamDataApiService {

  private final WebClient webClient;

  public ExamDataApiService(WebClient.Builder webClientBuilder) {
    this.webClient = webClientBuilder.baseUrl("https://sau-portal.de/exa-grad/exam-service").build();
  }

  public List<ExamDataDto> fetchAllExamData(){
    return webClient.get()
        .uri("/api/exams")
        .retrieve()
        .bodyToFlux(ExamDataDto.class)
        .collectList()
        .block();
  }

  public Mono<ExamDataDto> fetchOneExamData(String examId){
    return webClient.get()
        .uri("/api/exams/{id}", examId)
        .retrieve()
        .bodyToMono(ExamDataDto.class);
  }

}
