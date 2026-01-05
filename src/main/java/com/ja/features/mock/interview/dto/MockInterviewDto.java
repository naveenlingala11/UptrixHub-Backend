package com.ja.features.mock.interview.dto;

import java.time.LocalDateTime;

public record MockInterviewDto(
        Long id,
        String role,
        String status,
        LocalDateTime scheduledAt,
        String meetingLink,
        Integer score,
        String review
) {}
