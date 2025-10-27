package com.ase.exagrad.studentservice.service;

import com.ase.exagrad.studentservice.dto.response.LecturerFeedbackResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LecturerFeedbackService {

    private final RestTemplate restTemplate;

    @Value("${app.external-apis.lecturer-feedback.base-url}")
    private String baseUrl;
    

    public List<LecturerFeedbackResponseDto> getFeedbackForLecturer(String studentId) {
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .pathSegment(studentId)
                .toUriString();

        LecturerFeedbackResponseDto[] response = restTemplate.getForObject(url, LecturerFeedbackResponseDto[].class);
        return Arrays.asList(response != null ? response : new LecturerFeedbackResponseDto[0]);
    }
}
