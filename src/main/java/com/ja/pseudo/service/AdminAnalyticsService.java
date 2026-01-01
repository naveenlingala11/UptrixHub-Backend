package com.ja.pseudo.service;

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

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminAnalyticsService {

    private final PseudoTestAttemptRepository attemptRepo;
    private final PseudoAnswerLogRepository answerLogRepo;
    private final PseudoQuestionRepository questionRepo;

    public List<SkillAnalyticsResponse> skillAnalytics() {
        return attemptRepo.getSkillAnalytics();
    }

    public List<SubscriptionAnalyticsResponse> subscriptionAnalytics() {
        return attemptRepo.getSubscriptionStats();
    }

    public Page<WeakQuestionResponse> weakQuestions(int page, int size) {
        return answerLogRepo.getMostFailedQuestions(
                PageRequest.of(page, size)
        );
    }

}
