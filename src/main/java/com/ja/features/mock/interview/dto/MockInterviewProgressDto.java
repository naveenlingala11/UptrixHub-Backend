package com.ja.features.mock.interview.dto;

import java.util.List;

public record MockInterviewProgressDto(
        int score,
        int attempted,
        int total,
        String lastAttempt,
        List<String> strengths,
        List<String> weaknesses
) {}
