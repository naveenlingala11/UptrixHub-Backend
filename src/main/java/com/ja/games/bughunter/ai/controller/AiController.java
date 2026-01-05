package com.ja.games.bughunter.ai.controller;

import com.ja.games.bughunter.ai.dto.AiBugRequest;
import com.ja.games.bughunter.ai.dto.AiExplainResponse;
import com.ja.games.bughunter.ai.service.AiExplanationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiExplanationService service;

    @PostMapping("/explain-bug")
    public AiExplainResponse explain(@RequestBody AiBugRequest req) {

        if (req.userWasCorrect()) {
            return new AiExplainResponse(
                    "",
                    false
            );
        }

        return service.explainBug(
                req.questionId(),
                req.code(),
                req.bugType(),
                req.difficulty()
        );
    }
}
