package com.ja.home.home.dto;

import java.time.LocalDateTime;

public record MockInterviewProgressDto(
        int score,
        int attempted,
        int total,
        LocalDateTime upcoming
) {}
