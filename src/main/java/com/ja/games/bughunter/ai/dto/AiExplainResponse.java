package com.ja.games.bughunter.ai.dto;

public record AiExplainResponse(
        String explanation,
        boolean aiGenerated
) {}
