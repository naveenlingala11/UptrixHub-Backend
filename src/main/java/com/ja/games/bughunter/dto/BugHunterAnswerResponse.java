package com.ja.games.games.bughunter.dto;

public record BugHunterAnswerResponse(
        boolean correct,
        int earnedXp
) {}
