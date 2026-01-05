package com.ja.features.mock.interview.dto;

import java.time.LocalDateTime;
import java.util.List;

public record MockInterviewRequestDto(
        Long id,
        String role,
        String platform,
        String status,
        List<String> preferredSlots,
        LocalDateTime createdAt
) {}
