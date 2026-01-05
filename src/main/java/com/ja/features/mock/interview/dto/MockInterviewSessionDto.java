package com.ja.features.mock.interview.dto;

import java.time.LocalDateTime;
import java.util.List;

public record MockInterviewSessionDto(
        Long id,
        String role,
        String status,
        LocalDateTime scheduledAt,
        String meetingLink,
        Integer score,
        List<String> strengths,
        List<String> weaknesses,
        String review
) {}
