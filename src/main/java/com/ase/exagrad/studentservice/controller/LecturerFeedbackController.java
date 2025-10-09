package com.example.api.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/lecturer-feedback")
@CrossOrigin(origins = "https://sau-portal.de/exagrad-students")
public class LecturerFeedbackController {

    @Value("${lecturer.feedback.api.url:<<url>>}") // URL Eintragen
    private String externalApiUrl;

    private final RestTemplate restTemplate;

    public LecturerFeedbackController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public ResponseEntity<String> getAllFeedback() {
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                externalApiUrl,
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

    @GetMapping("/{id}")
    public ResponseEntity<String> getFeedbackById(@PathVariable String id) {
        try {
            String url = externalApiUrl + "/" + id;
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
                .body("Fehler beim Abrufen des Feedbacks: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<String> createFeedback(@RequestBody String feedbackData) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>(feedbackData, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                externalApiUrl,
                HttpMethod.POST,
                request,
                String.class
            );
            return ResponseEntity.status(response.getStatusCode())
                .body(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Fehler beim Erstellen des Feedbacks: " + e.getMessage());
        }
    }
}