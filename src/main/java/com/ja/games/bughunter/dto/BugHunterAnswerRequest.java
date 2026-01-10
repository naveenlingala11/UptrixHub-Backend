package com.ja.games.bughunter.dto;

public record BugHunterAnswerRequest(
        Long userId,
        Long questionId,
        String selectedAnswer
) {}
