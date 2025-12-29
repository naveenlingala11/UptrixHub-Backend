package com.ja.user.controller;

import com.ja.security.JwtUserPrincipal;
import com.ja.user.dto.UserPublicProfileResponse;
import com.ja.user.service.OgImageService;
import com.ja.user.service.UserService;
import com.ja.user.dto.UpdateProfileRequest;
import com.ja.user.dto.UserProfileResponse;
import com.ja.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final OgImageService ogImageService; // ✅ ADD THIS


    /* =========================
       CURRENT USER (LEGACY)
       (KEEP for dashboard auth)
    ========================== */
    @GetMapping("/me")
    public User me(@AuthenticationPrincipal JwtUserPrincipal principal) {
        return userService.getUserById(principal.getUserId());
    }

    /* =========================
       NEW DYNAMIC PROFILE
    ========================== */
    @GetMapping("/profile")
    public UserProfileResponse profile(
            @AuthenticationPrincipal JwtUserPrincipal principal
    ) {
        return userService.getProfile(principal.getUserId());
    }

    /* =========================
       UPDATE PROFILE
    ========================== */
    @PutMapping("/profile")
    public void updateProfile(
            @AuthenticationPrincipal JwtUserPrincipal principal,
            @RequestBody UpdateProfileRequest req
    ) {
        userService.updateProfile(principal.getUserId(), req);
    }

    @GetMapping("/public/profile/{id}")
    public UserPublicProfileResponse publicProfile(@PathVariable Long id) {
        return userService.getPublicProfileById(id);
    }

    @GetMapping("/profile/heatmap")
    public Map<String, Integer> heatmap(
            @AuthenticationPrincipal JwtUserPrincipal p
    ) {
        return userService.getHeatmap(p.getUserId())
                .entrySet().stream()
                .collect(Collectors.toMap(
                        e -> e.getKey().toString(),
                        Map.Entry::getValue
                ));
    }

    @GetMapping(
            value = "/public/profile/{id}/og",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public byte[] ogImage(@PathVariable Long id) {
        return ogImageService.generate(id);
    }

}
