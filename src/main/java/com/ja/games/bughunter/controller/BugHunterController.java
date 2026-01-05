package com.ja.games.bughunter.controller;

import com.ja.games.bughunter.dto.BugHunterAnswerRequest;
import com.ja.games.bughunter.dto.BugHunterAnswerResponse;
import com.ja.games.dto.SubmitBugAnswerRequest;
import com.ja.games.bughunter.entity.BugHunterAttempt;
import com.ja.games.bughunter.entity.BugHunterQuestion;
import com.ja.games.bughunter.service.BugHunterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bug-hunter")
@RequiredArgsConstructor
public class BugHunterController {

    private final BugHunterService service;

    // 🎮 Get random question
    @GetMapping("/question")
    public BugHunterQuestion getQuestion(
            @RequestParam(defaultValue = "JAVA") String language
    ) {
        return service.getRandomQuestion(language);
    }

    @PostMapping("/submit")
    public BugHunterAnswerResponse submit(
            @RequestBody BugHunterAnswerRequest request
    ) {
        return service.submitAnswer(request);
    }
}
