package com.ja.games.tictactoe.service;

import com.ja.games.repository.AchievementRepository;
import com.ja.games.service.GameAchievementBridge;
import com.ja.games.tictactoe.entity.TicTacToeStats;
import com.ja.games.tictactoe.repository.TicTacToeStatsRepository;
import com.ja.games.xp.service.XpEngineService;
import com.ja.games.xp.service.XpRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicTacToeService {

    private final TicTacToeStatsRepository repo;
    private final XpEngineService xpEngine;
    private final XpRuleService xpRuleService;
    private final AchievementRepository achievementRepo;
    private final GameAchievementBridge bridge;

    public int handleResult(Long userId, String result) {

        TicTacToeStats s = repo.findById(userId)
                .orElseGet(() -> {
                    TicTacToeStats t = new TicTacToeStats();
                    t.setUserId(userId);
                    return t;
                });

        int xp = 0;

        if ("WIN".equals(result)) {
            s.setWins(s.getWins() + 1);

            xp = xpRuleService.getXp("TIC_TAC_TOE", "WIN");

            xpEngine.addXp(
                    userId,
                    xp,
                    "TIC_TAC_TOE",
                    null,
                    "Won Tic Tac Toe"
            );

            if (s.getWins() >= 5) {
                achievementRepo.findAll()
                        .stream()
                        .filter(a -> a.getCode()
                                .equals("TIC_TAC_TOE_MASTER"))
                        .findFirst()
                        .ifPresent(a ->
                                bridge.unlockAchievement(userId, a)
                        );
            }

        } else if ("DRAW".equals(result)) {
            s.setDraws(s.getDraws() + 1);
        } else {
            s.setLosses(s.getLosses() + 1);
        }

        repo.save(s);
        return xp;
    }
}
