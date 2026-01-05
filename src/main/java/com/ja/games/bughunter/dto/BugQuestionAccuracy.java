package com.ja.games.bughunter.dto;

public record BugQuestionAccuracy(
        Long questionId,
        String title,
        String bugCategory,
        Long totalAttempts,
        Long correctAttempts,
        Double accuracy
) {}
