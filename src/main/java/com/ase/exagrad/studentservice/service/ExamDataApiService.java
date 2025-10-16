package com.ase.exagrad.studentservice.service;

import com.ase.exagrad.studentservice.dto.ExamData;
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

  public List<ExamData> fetchAllExamData(){
    return webClient.get()
        .uri("/api/exams")
        .retrieve()
        .bodyToFlux(ExamData.class)
        .collectList()
        .block();
  }

  public Mono<ExamData> fetchOneExamData(String examId){
    return webClient.get()
        .uri("/api/exams/{id}", examId)
        .retrieve()
        .bodyToMono(ExamData.class);
  }

}
