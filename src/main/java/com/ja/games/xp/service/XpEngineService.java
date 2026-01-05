package com.ja.games.xp.service;

import com.ja.games.entity.UserXp;
import com.ja.games.entity.XpTransaction;
import com.ja.games.repository.UserXpRepository;
import com.ja.games.repository.XpTransactionRepository;
import com.ja.games.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class XpEngineService {

    private final UserXpRepository userXpRepo;
    private final XpTransactionRepository txRepo;
    private final AchievementService achievementService;


    public void addXp(
            Long userId,
            int xp,
            String source,
            Long sourceId,
            String reason
    ) {
        UserXp ux = userXpRepo.findById(userId)
                .orElseGet(() -> createNew(userId));

        updateStreak(ux);

        ux.setTotalXp(ux.getTotalXp() + xp);
        ux.setLevel(calculateLevel(ux.getTotalXp()));

        userXpRepo.save(ux);

        XpTransaction tx = new XpTransaction();
        tx.setUserId(userId);
        tx.setXpEarned(xp);
        tx.setSource(source);
        tx.setSourceId(sourceId);
        tx.setReason(reason);

        txRepo.save(tx);

        // 🔥 THIS LINE WAS MISSING
        achievementService.checkAndUnlock(ux);
    }

    private UserXp createNew(Long userId) {
        UserXp ux = new UserXp();
        ux.setUserId(userId);
        ux.setLevel(1);
        ux.setTotalXp(0);
        ux.setCurrentStreak(0);
        ux.setLongestStreak(0);
        return ux;
    }

    private void updateStreak(UserXp ux) {
        LocalDate today = LocalDate.now();

        if (ux.getLastPlayed() == null) {
            ux.setCurrentStreak(1);
        } else if (ux.getLastPlayed().plusDays(1).equals(today)) {
            ux.setCurrentStreak(ux.getCurrentStreak() + 1);
        } else if (!ux.getLastPlayed().equals(today)) {
            ux.setCurrentStreak(1);
        }

        ux.setLongestStreak(
                Math.max(
                        ux.getLongestStreak(),
                        ux.getCurrentStreak()
                )
        );

        ux.setLastPlayed(today);

    }

    private int calculateLevel(int totalXp) {
        return (totalXp / 100) + 1;
    }
}
