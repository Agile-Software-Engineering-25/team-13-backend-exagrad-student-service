package com.ase.exagrad.studentservice.controller;

import com.ase.exagrad.studentservice.dto.response.LecturerFeedbackResponseDto;
import com.ase.exagrad.studentservice.service.LecturerFeedbackService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lecturer-feedback")
@Tag(name = "Lecturer Feedback", description = "Get Feedback and Grade for a student")
public class LecturerFeedbackController {

    private final LecturerFeedbackService feedbackService;

    @GetMapping("/{studentId}")
    public List<LecturerFeedbackResponseDto> getFeedbackForLecturer(@PathVariable("studentId") String studentId) {
        return feedbackService.getFeedbackForLecturer(studentId);
    }
}
