package com.ja.games.bughunter.service;

import com.ja.games.bughunter.dto.BugHunterPublicLeaderboardRow;
import com.ja.games.bughunter.repository.BugHunterAttemptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BugHunterPublicLeaderboardService {

    private final BugHunterAttemptRepository attemptRepo;

    public List<BugHunterPublicLeaderboardRow> top10() {
        return attemptRepo.top10Leaderboard(
                PageRequest.of(0, 10)
        );
    }
}
