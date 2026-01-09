package com.ja.pseudo.service;

import com.ja.admin.dto.ChallengeAnalyticsResponse;
import com.ja.challenge.entity.UserChallengeProgress;
import com.ja.challenge.repository.UserChallengeProgressRepository;
import com.ja.pseudo.dto.DifficultyAnalyticsResponse;
import com.ja.pseudo.dto.SkillAnalyticsResponse;
import com.ja.pseudo.dto.SubscriptionAnalyticsResponse;
import com.ja.pseudo.dto.WeakQuestionResponse;
import com.ja.pseudo.repository.PseudoAnswerLogRepository;
import com.ja.pseudo.repository.PseudoQuestionRepository;
import com.ja.pseudo.repository.PseudoTestAttemptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminAnalyticsService {

    private final PseudoTestAttemptRepository attemptRepo;
    private final PseudoAnswerLogRepository answerLogRepo;
    private final PseudoQuestionRepository questionRepo;
    private final UserChallengeProgressRepository repo;

    public List<SkillAnalyticsResponse> skillAnalytics() {
        return attemptRepo.getSkillAnalytics();
    }

    public List<SubscriptionAnalyticsResponse> subscriptionAnalytics() {
        return attemptRepo.getSubscriptionStats();
    }

    public List<DifficultyAnalyticsResponse> difficultyAnalytics() {
        return questionRepo.getDifficultyAnalytics();
    }

    public Page<WeakQuestionResponse> weakQuestions(int page, int size) {
        return answerLogRepo.getMostFailedQuestions(
                PageRequest.of(page, size)
        );
    }
    public ChallengeAnalyticsResponse todayAnalytics() {

        String today = LocalDate.now().toString();

        long total = repo.countByChallengeDate(today);
        long success = repo.countByChallengeDateAndSolvedTrue(today);

        List<UserChallengeProgress> all = repo.findByChallengeDate(today);

        double avgXp = all.stream()
                .mapToInt(UserChallengeProgress::getEarnedXp)
                .average()
                .orElse(0);

        double rate = total == 0 ? 0 : (success * 100.0) / total;

        return ChallengeAnalyticsResponse.builder()
                .totalSubmissions(total)
                .successfulSubmissions(success)
                .successRate(Math.round(rate * 100) / 100.0)
                .averageXp(Math.round(avgXp))
                .build();
    }
}
