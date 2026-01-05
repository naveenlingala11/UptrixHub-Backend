package com.ja.games.bughunter.dto;

public record BugHunterAnswerResponse(
        boolean correct,
        int earnedXp
) {}
