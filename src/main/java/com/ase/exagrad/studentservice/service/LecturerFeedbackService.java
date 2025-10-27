package com.ase.exagrad.studentservice.service;

import com.ase.exagrad.studentservice.dto.response.LecturerFeedbackResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LecturerFeedbackService {

    private final RestTemplate restTemplate;

    private final String baseUrl;
    @Value("${app.external-apis.lecturer-feedback.base-url}")

    public List<LecturerFeedbackResponseDto> getFeedbackForLecturer(String lecturerId) {
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .pathSegment(lecturerId)
                .toUriString();

        LecturerFeedbackResponseDto[] response = restTemplate.getForObject(url, LecturerFeedbackResponseDto[].class);
        return Arrays.asList(response != null ? response : new LecturerFeedbackResponseDto[0]);
    }
}
