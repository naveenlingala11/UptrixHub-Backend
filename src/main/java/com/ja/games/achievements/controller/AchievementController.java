package com.ja.games.achievements.controller;

import com.ja.games.dto.AchievementResponse;
import com.ja.games.entity.Achievement;
import com.ja.games.repository.AchievementRepository;
import com.ja.security.JwtUserPrincipal;
import com.ja.user.entity.UserAchievement;
import com.ja.user.repository.UserAchievementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/games/achievements")
@RequiredArgsConstructor
public class AchievementController {

    private final AchievementRepository achievementRepo;
    private final UserAchievementRepository userAchievementRepo;

    @GetMapping("/me")
    public List<AchievementResponse> myAchievements(
            @AuthenticationPrincipal JwtUserPrincipal user
    ) {
        List<UserAchievement> unlocked =
                userAchievementRepo.findByUserId(user.getUserId());

        return achievementRepo.findAll()
                .stream()
                .filter(Achievement::isEnabled)
                .map(a -> new AchievementResponse(
                        a.getCode(),
                        a.getTitle(),
                        a.getDescription(),
                        a.getIcon(),
                        unlocked.stream()
                                .anyMatch(u -> u.getTitle().equals(a.getTitle()))
                ))
                .toList();
    }

}
