package com.ja.challenge.controller;

import com.ja.challenge.dto.LeaderboardResponse;
import com.ja.challenge.dto.LeaderboardRow;
import com.ja.challenge.entity.UserStats;
import com.ja.challenge.repository.UserStatsRepository;
import com.ja.challenge.service.LeaderboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/challenges/leaderboard")
@CrossOrigin
@RequiredArgsConstructor
public class ChallengeLeaderboardController {

    private final LeaderboardService service;
    private final UserStatsRepository statsRepo;

    @GetMapping("/daily")
    public LeaderboardResponse daily() {
        return service.dailyLeaderboard();
    }

    @GetMapping("/global")
    public LeaderboardResponse global() {
        return service.globalLeaderboard();
    }

    @GetMapping
    public List<LeaderboardRow> top() {

        List<UserStats> stats = statsRepo
                .findTop50ByOrderByTotalXpDescCurrentStreakDesc();

        List<LeaderboardRow> result = new ArrayList<>();

        int rank = 1;
        for (UserStats s : stats) {
            result.add(
                    new LeaderboardRow(
                            rank++,
                            s.getUserId(),
                            s.getTotalXp(),
                            s.getCurrentStreak()
                    )
            );
        }
        return result;
    }
}
