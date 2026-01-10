package com.ja.challenge.service;

import com.ja.challenge.dto.DailyChallengeView;
import com.ja.challenge.entity.DailyChallenge;
import com.ja.challenge.repository.DailyChallengeRepository;
import com.ja.challenge.repository.UserChallengeProgressRepository;
import org.springframework.cache.annotation.Cacheable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class DailyChallengeQueryService {

    private final DailyChallengeRepository challengeRepo;
    private final UserChallengeProgressRepository progressRepo;

    @Cacheable(
            value = "daily-challenge",
            key = "#userId + ':' + T(java.time.LocalDate).now().toString()"
    )
    public DailyChallengeView getToday(Long userId) {

        String today = LocalDate.now().toString();

        DailyChallenge c = challengeRepo
                .findTodayWithCollections(today)
                .orElseThrow(() ->
                        new RuntimeException("❌ No daily challenge for " + today)
                );

        boolean locked = progressRepo
                .findByUserIdAndChallengeDate(userId, today)
                .isPresent();

        return DailyChallengeView.builder()
                .title(c.getTitle())
                .difficulty(c.getDifficulty())
                .time(c.getTime())
                .tags(new ArrayList<>(c.getTags()))
                .problem(c.getProblem())
                .starterCode(c.getStarterCode())
                .locked(locked)
                .xpEarned(locked ? c.getXpReward() : null)
                .build();
    }
}

