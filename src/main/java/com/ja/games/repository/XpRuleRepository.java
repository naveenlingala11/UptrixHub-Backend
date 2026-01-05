package com.ja.games.repository;

import com.ja.games.entity.XpRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface XpRuleRepository extends JpaRepository<XpRule, Long> {

    Optional<XpRule> findByGameTypeAndActionAndEnabledTrue(
            String gameType,
            String action
    );
}
