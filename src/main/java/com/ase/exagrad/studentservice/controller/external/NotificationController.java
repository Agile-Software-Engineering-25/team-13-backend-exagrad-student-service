package com.ase.exagrad.studentservice.controller.external;

import com.ase.exagrad.studentservice.dto.external.SendEmailRequest;
import com.ase.exagrad.studentservice.dto.external.SendEmailResponse;
import com.ase.exagrad.studentservice.service.external.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<SendEmailResponse> sendEmail(@Valid @RequestBody SendEmailRequest request) {
        SendEmailResponse response = notificationService.sendEmail(request);
        return ResponseEntity.ok(response);
    }
}
