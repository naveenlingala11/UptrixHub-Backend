package com.ja.pseudo.controller;

import com.ja.pseudo.dto.DifficultyAnalyticsResponse;
import com.ja.pseudo.dto.SkillAnalyticsResponse;
import com.ja.pseudo.dto.SubscriptionAnalyticsResponse;
import com.ja.pseudo.dto.WeakQuestionResponse;
import com.ja.pseudo.service.AdminAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/analytics")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminAnalyticsController {

    private final AdminAnalyticsService service;

    @GetMapping("/skills")
    public List<SkillAnalyticsResponse> skillAnalytics() {
        return service.skillAnalytics();
    }

    @GetMapping("/subscriptions")
    public List<SubscriptionAnalyticsResponse> subscriptionAnalytics() {
        return service.subscriptionAnalytics();
    }

    @GetMapping("/weak-questions")
    public Page<WeakQuestionResponse> weakQuestions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return service.weakQuestions(page, size);
    }
}
