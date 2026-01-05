package com.ja.features.resume.controller;

import com.ja.features.resume.dto.*;
import com.ja.features.resume.service.ResumeAnalysisService;
import com.ja.security.JwtUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/resume")
@RequiredArgsConstructor
public class ResumeAnalysisController {

    private final ResumeAnalysisService service;

    @PostMapping("/analyze")
    public ResumeAnalysisDto analyze(
            @AuthenticationPrincipal JwtUserPrincipal user,
            @RequestBody ResumeUploadRequest req
    ) {
        return service.analyze(user.getUserId(), req);
    }

    @GetMapping("/latest")
    public ResumeAnalysisDto latest(
            @AuthenticationPrincipal JwtUserPrincipal user
    ) {
        return service.latest(user.getUserId());
    }

    @GetMapping("/compare")
    public ResumeCompareDto compare(
            @AuthenticationPrincipal JwtUserPrincipal user
    ) {
        if (user == null) return null;
        return service.compare(user.getUserId());
    }

    // 🔒 ADMIN DASHBOARD
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public Object all() {
        return "Admin resume review dashboard (pagination later)";
    }

    @GetMapping("/home-summary")
    public ResumeHomeSummaryDto homeSummary(
            @AuthenticationPrincipal JwtUserPrincipal user
    ) {
        return service.homeSummary(user.getUserId());
    }

}
