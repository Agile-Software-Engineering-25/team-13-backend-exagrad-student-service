package com.example.api.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/lecturer-feedback")
@CrossOrigin(origins = "https://sau-portal.de/exagrad-students")
public class LecturerFeedbackController {

    @Value("${lecturer.feedback.api.url:https://sau-portal.de/exa-grad/grading-service/api/v1/feedback/for-lecturer}")
    private String externalApiUrl;

    private final RestTemplate restTemplate;

    public LecturerFeedbackController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/{lecturerId}")
    public ResponseEntity<String> getFeedbackForLecturer(@PathVariable String lecturerId) {
        try {
            String url = externalApiUrl + "/" + lecturerId;
            ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                String.class
            );
            return ResponseEntity.status(response.getStatusCode())
                .body(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Fehler beim Abrufen der Feedbacks: " + e.getMessage());
        }
    }
}