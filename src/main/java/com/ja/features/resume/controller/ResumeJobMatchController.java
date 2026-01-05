package com.ja.features.resume.controller;

import com.ja.features.resume.dto.ResumeJobMatchDto;
import com.ja.features.resume.dto.ResumeJobMatchRequest;
import com.ja.features.resume.service.ResumeJobMatchService;
import com.ja.security.JwtUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/resume-match")
@RequiredArgsConstructor
public class ResumeJobMatchController {

    private final ResumeJobMatchService service;

    @PostMapping("/analyze")
    public ResumeJobMatchDto analyze(
            @AuthenticationPrincipal JwtUserPrincipal user,
            @RequestBody ResumeJobMatchRequest req
    ) {
        return service.analyze(user.getUserId(), req);
    }

    @GetMapping("/latest")
    public ResumeJobMatchDto latest(
            @AuthenticationPrincipal JwtUserPrincipal user
    ) {
        return service.latest(user.getUserId());
    }

}
