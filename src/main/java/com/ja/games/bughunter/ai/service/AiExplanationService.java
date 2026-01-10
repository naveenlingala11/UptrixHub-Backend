package com.ja.games.bughunter.ai.service;


import com.ja.games.bughunter.ai.dto.AiExplainResponse;
import com.ja.games.bughunter.ai.dto.LlmClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiExplanationService {

    private final @Qualifier("bugHunterOpenAiClient") LlmClient llm;

    public AiExplainResponse explainBug(
            Long questionId,
            String code,
            String bugType,
            String difficulty
    ) {

        String prompt = """
        You are a senior Java engineer.

        Code:
        %s

        Bug Type:
        %s

        Difficulty:
        %s

        Explain:
        - What is the bug
        - Why it happens
        - How to fix it
        """.formatted(code, bugType, difficulty);

        String explanation = llm.generate(prompt);

        return new AiExplainResponse(explanation, true);
    }
}
