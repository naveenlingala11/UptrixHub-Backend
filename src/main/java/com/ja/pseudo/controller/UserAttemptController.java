package com.ja.pseudo.controller;

import com.ja.pseudo.dto.UserAttemptResponse;
import com.ja.pseudo.service.UserAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/attempts")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class UserAttemptController {

    private final UserAttemptService service;

    @GetMapping
    public Page<UserAttemptResponse> getAttempts(
            @RequestParam(required = false) String skill,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal(expression = "id") Long userId
    ) {
        return service.getUserAttempts(userId, skill, page, size);
    }
}

