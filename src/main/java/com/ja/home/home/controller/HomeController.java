package com.ja.home.home.controller;

import com.ja.features.resume.dto.ResumeHomeDetailDto;
import com.ja.features.resume.dto.ResumeHomeSummaryDto;
import com.ja.features.resume.service.ResumeAnalysisService;
import com.ja.home.home.dto.*;
import com.ja.home.home.service.HomeService;
import com.ja.security.JwtUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/home")
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;
    private final ResumeAnalysisService resumeAnalysisService;

    @GetMapping("/dashboard")
    public DashboardDto dashboard(
            @AuthenticationPrincipal JwtUserPrincipal principal
    ) {
        return homeService.getDashboard(principal.getUserId());
    }

    @GetMapping("/learning-path")
    public List<LearningPathDto> learningPath(
            @AuthenticationPrincipal JwtUserPrincipal principal
    ) {
        return homeService.getLearningPath(principal.getUserId());
    }

    @GetMapping("/activity")
    public List<ActivityHeatmapDto> activity(
            @AuthenticationPrincipal JwtUserPrincipal principal
    ) {
        return homeService.getHeatmap(principal.getUserId());
    }

    @GetMapping("/resume-summary")
    public ResumeHomeSummaryDto resumeSummary(
            @AuthenticationPrincipal JwtUserPrincipal principal
    ) {
        return resumeAnalysisService.homeSummary(principal.getUserId());
    }

    @GetMapping("/resume-detail")
    public ResumeHomeDetailDto resumeDetail(
            @AuthenticationPrincipal JwtUserPrincipal principal
    ) {
        return resumeAnalysisService.homeDetail(principal.getUserId());
    }

    @GetMapping("/mock-progress")
    public MockInterviewProgressDto mockProgress(
            @AuthenticationPrincipal JwtUserPrincipal principal
    ) {
        if (principal == null) {
            // fallback – NEVER crash dashboard
            return new MockInterviewProgressDto(
                    0,
                    0,
                    0,
                    null
            );
        }

        return homeService.getMockProgress(principal.getUserId());
    }

    @GetMapping("/salary")
    public SalaryInsightDto salary(
            @AuthenticationPrincipal JwtUserPrincipal principal
    ) {
        return homeService.getSalaryInsight(principal.getUserId());
    }
}

