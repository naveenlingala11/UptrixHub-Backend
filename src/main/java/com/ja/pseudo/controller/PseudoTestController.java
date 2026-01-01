package com.ja.pseudo.controller;

import com.ja.pseudo.dto.ResumeTestResponse;
import com.ja.pseudo.dto.StartTestResponse;
import com.ja.pseudo.dto.TestSubmitRequest;
import com.ja.pseudo.dto.TestSubmitResponse;
import com.ja.pseudo.service.PseudoTestService;
import com.ja.security.JwtUserPrincipal;
import com.ja.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api/pseudo-code")
@RequiredArgsConstructor
public class PseudoTestController {

    private final PseudoTestService service;

    @PostMapping("/start-test/{skill}")
    @PreAuthorize("isAuthenticated()")
    public StartTestResponse start(
            @PathVariable String skill,
            Authentication authentication
    ) {
        Long userId = getUserId(authentication);
        return service.startTest(skill, userId);
    }

    @GetMapping("/resume/{skill}")
    @PreAuthorize("isAuthenticated()")
    public ResumeTestResponse resume(
            @PathVariable String skill,
            Authentication authentication
    ) {
        Long userId = getUserId(authentication);
        return service.resumeTest(skill, userId);
    }

    @PostMapping("/submit-test/{attemptId}")
    @PreAuthorize("isAuthenticated()")
    public TestSubmitResponse submit(
            @PathVariable Long attemptId,
            @RequestBody TestSubmitRequest request,
            Authentication authentication
    ) {
        Long userId = getUserId(authentication);
        return service.submitTest(attemptId, request, userId);
    }

    @PostMapping("/violation/{attemptId}")
    public Map<String, Object> logViolation(
            @PathVariable Long attemptId,
            @RequestParam String type
    ) {
        long count = service.logViolation(attemptId, type);
        return Map.of(
                "count", count,
                "limit", 3,
                "blocked", count >= 3
        );
    }

    /* =============================
       🔐 USER ID RESOLUTION (FINAL)
    ============================== */
    private Long getUserId(Authentication auth) {

        if (auth == null || !auth.isAuthenticated()) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Not authenticated");
        }

        Object principal = auth.getPrincipal();

        // ✅ SESSION / FORM LOGIN
        if (principal instanceof UserPrincipal up) {
            return up.getId();
        }

        // ✅ JWT LOGIN (THIS WAS MISSING)
        if (principal instanceof JwtUserPrincipal jwt) {
            return jwt.getUserId();
        }

        throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Unsupported principal type: " + principal.getClass()
        );
    }
}
