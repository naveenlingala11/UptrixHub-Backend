package com.ja.games.service;

import com.ja.games.entity.Achievement;
import com.ja.games.entity.UserXp;
import com.ja.games.repository.AchievementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AchievementService {

    private final AchievementRepository repo;
    private final GameAchievementBridge bridge;

    public void checkAndUnlock(UserXp ux) {

        List<Achievement> all = repo.findAll();

        for (Achievement a : all) {

            if (!a.isEnabled()) continue;

            boolean xpOk = a.getRequiredXp() == null ||
                    ux.getTotalXp() >= a.getRequiredXp();

            boolean streakOk = a.getRequiredStreak() == null ||
                    ux.getCurrentStreak() >= a.getRequiredStreak();

            if (xpOk && streakOk) {
                bridge.unlockAchievement(ux.getUserId(), a);
            }
        }
    }
}

