package com.ja.games.bughunter.dto;

public record BugHunterPublicLeaderboardRow(
        Long userId,
        String name,
        Long totalAttempts,
        Long correctAttempts,
        Double accuracy,
        Long totalXp
) {}
