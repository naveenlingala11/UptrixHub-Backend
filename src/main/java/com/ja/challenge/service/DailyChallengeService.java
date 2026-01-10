package com.ja.challenge.service;

import com.ja.challenge.dto.DailyChallengeResponse;
import com.ja.challenge.dto.SubmitChallengeRequest;
import com.ja.challenge.dto.TestCaseDto;
import com.ja.challenge.entity.DailyChallenge;
import com.ja.challenge.entity.UserChallengeProgress;
import com.ja.challenge.repository.DailyChallengeRepository;
import com.ja.challenge.repository.UserChallengeProgressRepository;
import com.ja.code.dto.TestCase;
import com.ja.code.runner.JavaRunner;
import com.ja.code.runner.ProcessResult;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DailyChallengeService {

    private final DailyChallengeRepository challengeRepo;
    private final UserChallengeProgressRepository progressRepo;
    private final LeaderboardService leaderboardService;

    @Transactional
    @CacheEvict(value = "daily-challenge", allEntries = true)
    public DailyChallengeResponse submit(
            Long userId,
            SubmitChallengeRequest req
    ) throws Exception {

        String today = LocalDate.now().toString();

        /* 🔒 LOCK CHECK */
        if (progressRepo.findByUserIdAndChallengeDate(userId, today).isPresent()) {
            return DailyChallengeResponse.builder()
                    .success(false)
                    .locked(true)
                    .message("🚫 You already submitted today’s challenge")
                    .build();
        }

        DailyChallenge challenge = challengeRepo
                .findTodayWithCollections(today)
                .orElseThrow(() -> new RuntimeException("No challenge today"));

        /* ✅ CONVERT DTO → RUNNER TEST CASE */
        List<TestCase> tests = toRunnerTests(challenge.getTests());

        int passed = 0;

        for (TestCase t : tests) {

            ProcessResult r = JavaRunner.run(
                    req.getCode(),
                    t.getInput(),
                    req.getJavaVersion()
            );

            if (!r.output().trim().equals(t.getExpectedOutput().trim())) {
                return DailyChallengeResponse.builder()
                        .success(false)
                        .locked(false)
                        .verdict("❌ Wrong Answer")
                        .message("Test case failed")
                        .build();
            }

            passed++;
        }

        /* ✅ SAVE & LOCK */
        UserChallengeProgress progress = new UserChallengeProgress();
        progress.setUserId(userId);
        progress.setChallengeDate(today);
        progress.setSolved(true);
        progress.setEarnedXp(challenge.getXpReward());
        progress.setSubmittedAt(LocalDateTime.now());

        progressRepo.save(progress);

        /* 🔥 LEADERBOARD UPDATE */
        leaderboardService.updateOnSolve(
                userId,
                challenge.getXpReward()
        );

        return DailyChallengeResponse.builder()
                .success(true)
                .locked(true)
                .verdict("✅ Accepted (" + passed + "/" + tests.size() + ")")
                .xpEarned(challenge.getXpReward())
                .message("🎉 Challenge solved & locked!")
                .build();
    }

    /* 🔁 DTO → RUNNER CONVERSION */
    private List<TestCase> toRunnerTests(List<TestCaseDto> tests) {
        return tests.stream()
                .map(t -> new TestCase(
                        t.getInput(),
                        t.getExpectedOutput()
                ))
                .toList();
    }
}
