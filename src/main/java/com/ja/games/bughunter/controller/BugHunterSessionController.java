package com.ja.games.games.bughunter.controller;

import com.ja.games.games.bughunter.entity.BugHunterQuestion;
import com.ja.games.games.bughunter.repository.BugHunterQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/games/bug-hunter/session")
@RequiredArgsConstructor
public class BugHunterSessionController {

    private final BugHunterQuestionRepository repo;

    @GetMapping
    public List<BugHunterQuestion> session(
            @RequestParam String language
    ) {
        return Stream.of(
                repo.random(language, "EASY", 10),
                repo.random(language, "MEDIUM", 8),
                repo.random(language, "HARD", 7)
        ).flatMap(List::stream).toList();
    }
}
