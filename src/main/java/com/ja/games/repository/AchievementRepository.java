package com.ja.games.repository;

import com.ja.games.entity.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AchievementRepository
        extends JpaRepository<Achievement, Long> {
}
