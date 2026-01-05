package com.ja.games.service;

import com.ja.games.entity.Achievement;
import com.ja.user.repository.UserAchievementRepository;
import com.ja.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameAchievementBridge {

    private final UserService userService;
    private final UserAchievementRepository achievementRepo;

    @Transactional
    public void unlockAchievement(Long userId, Achievement a) {

        boolean alreadyUnlocked =
                achievementRepo.existsByUserIdAndTitle(userId, a.getTitle());

        if (alreadyUnlocked) return;

        userService.addAchievement(
                userId,
                a.getTitle(),
                a.getDescription()
        );
    }
}

