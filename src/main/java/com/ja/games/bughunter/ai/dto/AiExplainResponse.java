package com.ja.games.games.bughunter.ai.dto;

public record AiExplainResponse(
        String explanation,
        boolean aiGenerated
) {}
