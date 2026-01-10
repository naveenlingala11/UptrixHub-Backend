package com.ja.games.tictactoe.service;

import com.ja.games.repository.AchievementRepository;
import com.ja.games.service.GameAchievementBridge;
import com.ja.games.tictactoe.entity.TicTacToeStats;
import com.ja.games.tictactoe.repository.TicTacToeStatsRepository;
import com.ja.games.xp.service.XpEngineService;
import com.ja.games.xp.service.XpRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TicTacToeXpService {

    private final TicTacToeStatsRepository statsRepo;
    private final XpEngineService xpEngine;
    private final XpRuleService xpRuleService;
    private final AchievementRepository achievementRepo;
    private final GameAchievementBridge achievementBridge;

    @Transactional
    public void handleOnlineWin(Long userId) {

        TicTacToeStats s = statsRepo.findById(userId)
                .orElseGet(() -> {
                    TicTacToeStats t = new TicTacToeStats();
                    t.setUserId(userId);
                    return t;
                });

        s.setOnlineWins(s.getOnlineWins() + 1);
        statsRepo.save(s);

        int xp = xpRuleService.getXp(
                "TIC_TAC_TOE",
                "ONLINE_WIN"
        );

        xpEngine.addXp(
                userId,
                xp,
                "TIC_TAC_TOE_ONLINE",
                null,
                "🏆 Online Match Victory"
        );

        // 🔓 Achievement unlock (10 wins)
        if (s.getOnlineWins() >= 10) {
            achievementRepo.findAll()
                    .stream()
                    .filter(a ->
                            "TIC_TAC_TOE_ONLINE_MASTER"
                                    .equals(a.getCode())
                    )
                    .findFirst()
                    .ifPresent(a ->
                            achievementBridge.unlockAchievement(userId, a)
                    );
        }
    }
}
