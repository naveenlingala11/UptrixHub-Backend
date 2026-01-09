package com.ja.games.games.bughunter.controller;

import com.ja.games.games.bughunter.dto.BugHunterPublicLeaderboardRow;
import com.ja.games.games.bughunter.service.BugHunterPublicLeaderboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/bug-hunter/public")
@RequiredArgsConstructor
public class BugHunterPublicController {

    private final BugHunterPublicLeaderboardService service;

    @GetMapping("/leaderboard")
    public List<BugHunterPublicLeaderboardRow> leaderboard() {
        return service.top10();
    }
}
