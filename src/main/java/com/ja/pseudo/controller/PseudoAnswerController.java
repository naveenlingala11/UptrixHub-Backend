package com.ja.pseudo.controller;

import com.ja.pseudo.dto.AnswerSubmitRequest;
import com.ja.pseudo.dto.AnswerSubmitResponse;
import com.ja.pseudo.service.PseudoAnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pseudo-code")
@RequiredArgsConstructor
public class PseudoAnswerController {

    private final PseudoAnswerService service;

    // 🔓 PUBLIC (no login required)
    @PostMapping("/answer")
    public AnswerSubmitResponse submitAnswer(
            @RequestBody AnswerSubmitRequest request) {

        return service.checkAnswer(request);
    }

    @PostMapping("/save-answer/{attemptId}")
    public void saveAnswer(
            @PathVariable Long attemptId,
            @RequestBody AnswerSubmitRequest req
    ) {
        service.saveAnswer(attemptId, req);
    }

}
