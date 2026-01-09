package com.ja.games.games.bughunter.repository;

import com.ja.games.games.bughunter.dto.BugCategoryAnalytics;
import com.ja.games.games.bughunter.dto.BugHunterLeaderboardRow;
import com.ja.games.games.bughunter.dto.BugHunterPublicLeaderboardRow;
import com.ja.games.games.bughunter.dto.BugQuestionAccuracy;
import com.ja.games.games.bughunter.entity.BugHunterAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BugHunterAttemptRepository
        extends JpaRepository<BugHunterAttempt, Long> {

    @Query("""
        SELECT new com.ja.games.bughunter.dto.BugCategoryAnalytics(
            a.bugCategory,
            COUNT(a),
            SUM(CASE WHEN a.correct = false THEN 1 ELSE 0 END),
            (SUM(CASE WHEN a.correct = false THEN 1 ELSE 0 END) * 100.0 / COUNT(a))
        )
        FROM BugHunterAttempt a
        GROUP BY a.bugCategory
    """)
    List<BugCategoryAnalytics> categoryAnalytics();


    @Query("""
    SELECT new com.ja.games.bughunter.dto.BugQuestionAccuracy(
        q.id,
        q.title,
        q.bugCategory,
        COUNT(a),
        SUM(CASE WHEN a.correct = true THEN 1 ELSE 0 END),
        CAST(
          (SUM(CASE WHEN a.correct = true THEN 1 ELSE 0 END) * 100.0 / COUNT(a))
          AS double
        )
    )
    FROM BugHunterAttempt a
    JOIN BugHunterQuestion q ON q.id = a.questionId
    GROUP BY q.id, q.title, q.bugCategory
    ORDER BY
      CAST(
        (SUM(CASE WHEN a.correct = true THEN 1 ELSE 0 END) * 100.0 / COUNT(a))
        AS double
      )
      ASC
""")
    List<BugQuestionAccuracy> questionAccuracy();

    List<BugHunterAttempt>
    findByUserIdOrderByAttemptedAtDesc(Long userId);

    @Query("""
    SELECT new com.ja.games.bughunter.dto.BugHunterLeaderboardRow(
        u.id,
        u.name,
        COUNT(a),
        SUM(CASE WHEN a.correct = true THEN 1 ELSE 0 END),
        CAST(
            (SUM(CASE WHEN a.correct = true THEN 1 ELSE 0 END) * 100.0 / COUNT(a))
            AS double
        ),
        SUM(a.earnedXp)
    )
    FROM BugHunterAttempt a
    JOIN User u ON u.id = a.userId
    GROUP BY u.id, u.name
    ORDER BY
        SUM(a.earnedXp) DESC,
        CAST(
            (SUM(CASE WHEN a.correct = true THEN 1 ELSE 0 END) * 100.0 / COUNT(a))
            AS double
        ) DESC
""")
    List<BugHunterLeaderboardRow> leaderboard();

    @Query("""
    SELECT new com.ja.games.bughunter.dto.BugHunterPublicLeaderboardRow(
        u.id,
        u.name,
        COUNT(a),
        SUM(CASE WHEN a.correct = true THEN 1 ELSE 0 END),
        CAST(
            (SUM(CASE WHEN a.correct = true THEN 1 ELSE 0 END) * 100.0 / COUNT(a))
            AS double
        ),
        SUM(a.earnedXp)
    )
    FROM BugHunterAttempt a
    JOIN User u ON u.id = a.userId
    GROUP BY u.id, u.name
    ORDER BY
        SUM(a.earnedXp) DESC,
        CAST(
            (SUM(CASE WHEN a.correct = true THEN 1 ELSE 0 END) * 100.0 / COUNT(a))
            AS double
        ) DESC
""")
    List<BugHunterPublicLeaderboardRow> top10Leaderboard(
            org.springframework.data.domain.Pageable pageable
    );

}
