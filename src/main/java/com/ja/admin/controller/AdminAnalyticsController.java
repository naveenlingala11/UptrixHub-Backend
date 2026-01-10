package com.ja.admin.controller;

import com.ja.admin.service.AdminAnalyticsService;
import com.ja.pseudo.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/difficulty")
    public List<DifficultyAnalyticsResponse> difficultyAnalytics() {
        return service.difficultyAnalytics();
    }

    @GetMapping("/weak-questions")
    public Page<WeakQuestionResponse> weakQuestions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return service.weakQuestions(page, size);
    }
}
