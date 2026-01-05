package com.ja.home.learning.controller;

import com.ja.home.learning.dto.ContinueLearningDto;
import com.ja.home.learning.service.ContinueLearningService;
import com.ja.security.JwtUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/home")
@RequiredArgsConstructor
public class ContinueLearningController {

    private final ContinueLearningService service;

    @GetMapping("/continue-learning")
    public ContinueLearningDto continueLearning(
            @AuthenticationPrincipal JwtUserPrincipal principal
    ) {
        if (principal == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "User not authenticated"
            );
        }
        return service.get(principal.getUserId());
    }

}
