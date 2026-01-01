package com.ja.pseudo.controller;

import com.ja.pseudo.entity.PseudoQuestion;
import com.ja.pseudo.service.PseudoCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/api/pseudo-code")
@RequiredArgsConstructor
public class PseudoQuestionController {

    private final PseudoCodeService service;

    @GetMapping("/questions/{skill}")
    public Page<PseudoQuestion> getQuestions(
            @PathVariable String skill,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal Object principal
    ) {
        Long userId = null;

        if (principal instanceof com.ja.security.UserPrincipal up) {
            userId = up.getId();
        }

        return service.getQuestions(skill, page, size, userId);
    }
}

