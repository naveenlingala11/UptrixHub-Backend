package com.ja.games.games.bughunter.dto;

public record BugCategoryAnalytics(
        String bugCategory,
        long totalAttempts,
        long wrongAttempts,
        double wrongPercentage
) {}
