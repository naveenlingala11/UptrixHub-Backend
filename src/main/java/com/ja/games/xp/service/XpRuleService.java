package com.ja.games.xp.service;

import com.ja.games.repository.XpRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class XpRuleService {

    private final XpRuleRepository repo;

    public int getXp(String gameType, String action) {

        return repo.findByGameTypeAndActionAndEnabledTrue(gameType, action)
                .map(r -> Math.max(r.getXp(), 0)) // SAFETY
                .orElse(0);
    }
}
