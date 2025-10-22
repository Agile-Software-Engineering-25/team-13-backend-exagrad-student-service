package com.example.lecturerfeedback.controller;

import com.example.lecturerfeedback.dto.LecturerFeedbackDto;
import com.example.lecturerfeedback.service.LecturerFeedbackService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lecturer-feedback")
public class LecturerFeedbackController {

    private final LecturerFeedbackService feedbackService;

    @GetMapping("/{lecturerId}")
    public List<LecturerFeedbackDto> getFeedbackForLecturer(@PathVariable("lecturerId") String lecturerId) {
        return feedbackService.getFeedbackForLecturer(lecturerId);
    }
}
