package com.ja.admin.controller;

import com.ja.security.JwtUtil;
import com.ja.security.JwtUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/preview")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminPreviewController {

    private final JwtUtil jwtUtil;

    @PostMapping("/{userId}")
    public Map<String, String> previewUser(
            @PathVariable Long userId,
            @AuthenticationPrincipal JwtUserPrincipal admin
    ) {
        String token = jwtUtil.generateImpersonationToken(
                userId,
                admin.getUserId()
        );

        return Map.of("token", token);
    }
}
