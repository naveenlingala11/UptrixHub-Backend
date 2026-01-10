package com.ja.challenge.repository;

import com.ja.challenge.entity.UserStats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserStatsRepository extends JpaRepository<UserStats, Long> {

    List<UserStats> findTop50ByOrderByTotalXpDescCurrentStreakDesc();
}
