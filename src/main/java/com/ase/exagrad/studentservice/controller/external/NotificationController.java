package com.ase.exagrad.studentservice.controller;

import com.ase.exagrad.studentservice.dto.request.SendEmailRequest;
import com.ase.exagrad.studentservice.dto.response.SendEmailResponse;
import com.ase.exagrad.studentservice.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/send")
    public ResponseEntity<SendEmailResponse> sendEmail(@Valid @RequestBody SendEmailRequest request) {
        SendEmailResponse response = notificationService.sendEmail(request);
        return ResponseEntity.ok(response);
    }
}
