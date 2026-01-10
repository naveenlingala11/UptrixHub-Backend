package com.ja.challenge.repository;

import com.ja.challenge.entity.DailyChallenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DailyChallengeRepository
        extends JpaRepository<DailyChallenge, Long> {

    Optional<DailyChallenge> findByChallengeDate(String challengeDate);

    boolean existsByChallengeDate(String challengeDate);

    // Used ONLY for read (query side)
    @Query("""
    SELECT c
    FROM DailyChallenge c
    LEFT JOIN FETCH c.tags
    LEFT JOIN FETCH c.problem
    WHERE c.challengeDate = :date
""")
    Optional<DailyChallenge> findTodayWithCollections(String date);

}
