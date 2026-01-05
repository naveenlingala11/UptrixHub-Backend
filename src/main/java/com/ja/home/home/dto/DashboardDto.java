package com.ja.home.home.dto;

public record DashboardDto(
        int activeKits,
        int overallProgress,
        int questionsSolved,
        int streakDays
) {}
