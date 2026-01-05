package com.ja.ai;

import com.ja.games.bughunter.ai.dto.LlmClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@Primary
@RequiredArgsConstructor
public class OpenAiLlmClient implements LlmClient {

    private final RestTemplate restTemplate;

    @Value("${ai.openai.api-key}")
    private String apiKey;

    @Override
    public String generate(String prompt) {

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(apiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> body = Map.of(
                    "model", "gpt-4o-mini",
                    "messages", List.of(
                            Map.of("role", "user", "content", prompt)
                    ),
                    "temperature", 0.4
            );

            HttpEntity<Map<String, Object>> entity =
                    new HttpEntity<>(body, headers);

            Map response = restTemplate.postForObject(
                    "https://api.openai.com/v1/chat/completions",
                    entity,
                    Map.class
            );

            return extractText(response);

        } catch (Exception e) {
            return """
            ⚠️ AI Explanation Unavailable

            The AI service is currently unavailable.
            Please review the bug explanation manually.
            """;
        }
    }

    private String extractText(Map response) {

        List choices = (List) response.get("choices");
        Map choice = (Map) choices.get(0);
        Map message = (Map) choice.get("message");

        return message.get("content").toString();
    }
}
