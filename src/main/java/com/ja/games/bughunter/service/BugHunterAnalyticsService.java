package com.ja.games.games.bughunter.service;

import com.ja.games.games.bughunter.dto.BugHunterLeaderboardRow;
import com.ja.games.games.bughunter.repository.BugHunterAttemptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BugHunterAnalyticsService {

    private final BugHunterAttemptRepository attemptRepo;

    public List<BugHunterLeaderboardRow> leaderboard() {
        return attemptRepo.leaderboard();
    }
}
