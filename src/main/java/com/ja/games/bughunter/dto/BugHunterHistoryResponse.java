package com.ja.games.bughunter.dto;

import java.time.LocalDateTime;

public record BugHunterHistoryResponse(
        Long questionId,
        String bugCategory,
        String selectedAnswer,
        boolean correct,
        int earnedXp,
        LocalDateTime attemptedAt
) {}
