package com.ja.challenge.repository;

import com.ja.challenge.dto.DailyTrendPoint;
import com.ja.challenge.entity.UserChallengeProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserChallengeProgressRepository
        extends JpaRepository<UserChallengeProgress, Long> {

    Optional<UserChallengeProgress>
    findByUserIdAndChallengeDate(Long userId, String challengeDate);

    // 🔥 DAILY LEADERBOARD
    @Query("""
        SELECT u.userId, SUM(u.earnedXp)
        FROM UserChallengeProgress u
        WHERE u.challengeDate = :date
        GROUP BY u.userId
        ORDER BY SUM(u.earnedXp) DESC
    """)
    List<Object[]> dailyLeaderboard(String date);

    // 🔥 GLOBAL LEADERBOARD
    @Query("""
        SELECT u.userId, SUM(u.earnedXp)
        FROM UserChallengeProgress u
        GROUP BY u.userId
        ORDER BY SUM(u.earnedXp) DESC
    """)
    List<Object[]> globalLeaderboard();

    // 🔥 Last N solved days (DESC order)
    @Query("""
        SELECT u.challengeDate
        FROM UserChallengeProgress u
        WHERE u.userId = :userId
          AND u.solved = true
        ORDER BY u.challengeDate DESC
    """)
    List<String> findSolvedDates(Long userId);

    // 🔥 Total XP
    @Query("""
        SELECT COALESCE(SUM(u.earnedXp), 0)
        FROM UserChallengeProgress u
        WHERE u.userId = :userId
    """)
    int totalXp(Long userId);

    long countByChallengeDate(String challengeDate);

    long countByChallengeDateAndSolvedTrue(String date);

    List<UserChallengeProgress> findByChallengeDate(String date);

    @Query("""
SELECT new com.ja.challenge.dto.DailyTrendPoint(
  u.challengeDate,
  COUNT(u)
)
FROM UserChallengeProgress u
GROUP BY u.challengeDate
ORDER BY u.challengeDate
""")
    List<DailyTrendPoint> dailyTrend();

    List<UserChallengeProgress>
    findBySubmittedAtBetween(LocalDateTime from, LocalDateTime to);
}
