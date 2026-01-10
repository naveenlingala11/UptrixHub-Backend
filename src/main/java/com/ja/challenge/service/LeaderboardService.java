package com.ja.challenge.service;

import com.ja.challenge.dto.*;
import com.ja.challenge.entity.UserStats;
import com.ja.challenge.repository.UserChallengeProgressRepository;
import com.ja.challenge.repository.UserStatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaderboardService {

    private final UserChallengeProgressRepository repo;
    private final UserStatsRepository statsRepo;

    /* ================= DAILY ================= */
    public LeaderboardResponse dailyLeaderboard() {

        String today = LocalDate.now().toString();
        List<Object[]> rows = repo.dailyLeaderboard(today);

        return buildResponse("DAILY", rows);
    }

    /* ================= GLOBAL ================= */
    public LeaderboardResponse globalLeaderboard() {

        List<Object[]> rows = repo.globalLeaderboard();
        return buildResponse("GLOBAL", rows);
    }

    /* ================= COMMON ================= */
    private LeaderboardResponse buildResponse(
            String type,
            List<Object[]> rows
    ) {
        List<LeaderboardEntryDto> list = new ArrayList<>();
        int rank = 1;

        for (Object[] r : rows) {
            list.add(new LeaderboardEntryDto(
                    (Long) r[0],
                    ((Number) r[1]).intValue(),
                    rank++
            ));
        }

        LeaderboardResponse res = new LeaderboardResponse();
        res.setType(type);
        res.setLeaders(list);
        return res;
    }

    public void updateOnSolve(Long userId, int earnedXp) {

        String today = LocalDate.now().toString();
        String yesterday = LocalDate.now().minusDays(1).toString();

        UserStats stats = statsRepo.findById(userId)
                .orElseGet(() -> {
                    UserStats s = new UserStats();
                    s.setUserId(userId);
                    s.setTotalXp(0);
                    s.setCurrentStreak(0);
                    s.setMaxStreak(0);
                    return s;
                });

        // 🔥 XP
        stats.setTotalXp(stats.getTotalXp() + earnedXp);

        // 🔥 STREAK LOGIC
        if (yesterday.equals(stats.getLastSolvedDate())) {
            stats.setCurrentStreak(stats.getCurrentStreak() + 1);
        } else {
            stats.setCurrentStreak(1);
        }

        // 🔥 MAX STREAK
        stats.setMaxStreak(
                Math.max(stats.getMaxStreak(), stats.getCurrentStreak())
        );

        stats.setLastSolvedDate(today);

        statsRepo.save(stats);
    }
}
