package com.ase.exagrad.studentservice.controller.external;

import com.ase.exagrad.studentservice.dto.external.ExamFeedbackResponseDto;
import com.ase.exagrad.studentservice.service.external.FeedbackService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lecturer-feedback")
@Tag(name = "Lecturer Feedback", description = "Get Feedback and Grade for a student")
public class FeedbackController {

  private final FeedbackService feedbackService;

  @GetMapping("/{studentId}")
  public List<ExamFeedbackResponseDto> getAllFeedbackForStudent(
      @PathVariable("studentId") String studentId) {
    return feedbackService.getAllFeedbackForStudent(studentId);
  }
}
