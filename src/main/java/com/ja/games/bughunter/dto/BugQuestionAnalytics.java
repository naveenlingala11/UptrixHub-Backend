package com.ja.games.bughunter.dto;

public record BugQuestionAnalytics(
        Long questionId,
        String title,
        long attempts,
        long correct,
        Number accuracy
) {}
