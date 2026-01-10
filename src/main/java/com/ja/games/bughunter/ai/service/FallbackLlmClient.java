package com.ja.games.bughunter.ai.service;

import com.ja.games.bughunter.ai.dto.LlmClient;
import org.springframework.stereotype.Service;

@Service
public class FallbackLlmClient implements LlmClient {

    @Override
    public String generate(String prompt) {
        return """
        🔧 AI explanation unavailable right now.

        Explanation (fallback):
        - Identify shared locks
        - Ensure consistent lock ordering
        - Avoid nested synchronized blocks
        """;
    }
}
