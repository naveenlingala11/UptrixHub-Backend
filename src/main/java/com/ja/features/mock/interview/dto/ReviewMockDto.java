package com.ja.features.mock.interview.dto;

import java.util.List;

public record ReviewMockDto(
        int score,
        List<String> strengths,
        List<String> weaknesses,
        String review
) {}
