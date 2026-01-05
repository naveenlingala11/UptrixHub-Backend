package com.ja.games.bughunter.ai.dto;

public record AiBugRequest(
        Long questionId,
        String code,
        String bugType,
        String difficulty,
        boolean userWasCorrect
) {}
