package com.ase.exagrad.studentservice.controller;

import com.ase.exagrad.studentservice.dto.response.LecturerFeedbackResponseDto;
import com.ase.exagrad.studentservice.service.LecturerFeedbackService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/lecturer-feedback")
@Tag(name = "Lecturer Feedback", description = "Get Feedback and Grade from a Lecturer")
public class LecturerFeedbackController {

    private final LecturerFeedbackService feedbackService;

    public LecturerFeedbackController(LecturerFeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping("/{lecturerId}")
    public List<LecturerFeedbackResponseDto> getFeedbackForLecturer(@PathVariable("lecturerId") String lecturerId) {
        return feedbackService.getFeedbackForLecturer(lecturerId);
    }
}
