package com.ase.exagrad.studentservice.controller.external;

import com.ase.exagrad.studentservice.component.ApiResponseFactory;
import com.ase.exagrad.studentservice.dto.external.ExamFeedbackResponseDto;
import com.ase.exagrad.studentservice.dto.response.ApiResponseWrapper;
import com.ase.exagrad.studentservice.service.external.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lecturer-feedback")
@Tag(name = "Lecturer Feedback", description = "Get Feedback and Grade for a student")
@Validated
public class FeedbackController {

  private final FeedbackService feedbackService;
  private final ApiResponseFactory apiResponseFactory;

  @GetMapping("/{studentId}")
  @Operation(
      summary = "Get all feedback for a student",
      description =
          "Retrieves all exam feedback and grades for a specific student from the external lecturer feedback service")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved feedback",
            content = @Content(schema = @Schema(implementation = ExamFeedbackResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid student ID format"),
        @ApiResponse(
            responseCode = "500",
            description = "Failed to retrieve feedback from external service")
      })
  public ResponseEntity<ApiResponseWrapper<List<ExamFeedbackResponseDto>>> getAllFeedbackForStudent(
      @Parameter(
              description = "Student ID (alphanumeric with hyphens and underscores allowed)",
              example = "f2a26e3f-3b50-44ac-a7f9-02fe3b41cf6a")
          @PathVariable("studentId")
          @Pattern(
              regexp = "^[a-zA-Z0-9_-]+$",
              message = "Student ID must be alphanumeric with optional hyphens and underscores")
          String studentId,
      HttpServletRequest request) {
    List<ExamFeedbackResponseDto> feedback = feedbackService.getAllFeedbackForStudent(studentId);
    return ResponseEntity.ok(apiResponseFactory.success(feedback, request.getRequestURI()));
  }
}
