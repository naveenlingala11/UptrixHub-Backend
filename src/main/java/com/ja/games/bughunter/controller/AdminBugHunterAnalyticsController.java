package com.ja.games.bughunter.controller;

import com.ja.games.bughunter.dto.BugHunterLeaderboardRow;
import com.ja.games.bughunter.service.BugHunterAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/bug-hunter/analytics")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminBugHunterAnalyticsController {

    private final BugHunterAnalyticsService service;

    @GetMapping("/leaderboard")
    public List<BugHunterLeaderboardRow> leaderboard() {
        return service.leaderboard();
    }
}
