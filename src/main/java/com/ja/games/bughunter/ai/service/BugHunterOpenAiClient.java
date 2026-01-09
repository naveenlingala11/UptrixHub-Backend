package com.ja.games.games.bughunter.ai.service;

import com.ja.games.games.bughunter.ai.dto.LlmClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service("bugHunterOpenAiClient")
public class BugHunterOpenAiClient implements LlmClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${ai.openai.api-key}")
    private String apiKey;

    @Override
    public String generate(String prompt) {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
                "model", "gpt-4o-mini",
                "messages", List.of(
                        Map.of("role", "user", "content", prompt)
                )
        );

        HttpEntity<?> entity = new HttpEntity<>(body, headers);

        Map<?, ?> res = restTemplate.postForObject(
                "https://api.openai.com/v1/chat/completions",
                entity,
                Map.class
        );

        return ((Map<?, ?>)
                ((List<?>) res.get("choices")).get(0))
                .get("message")
                .toString();
    }
}
