    package com.ja.pseudo.repository;

    import com.ja.pseudo.dto.SkillAnalyticsResponse;
    import com.ja.pseudo.dto.SubscriptionAnalyticsResponse;
    import com.ja.pseudo.entity.PseudoTestAttempt;

    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.jpa.repository.Query;

    import java.util.List;
    import java.util.Optional;

    public interface PseudoTestAttemptRepository
            extends JpaRepository<PseudoTestAttempt, Long> {

        Page<PseudoTestAttempt> findByUserIdOrderByCreatedAtDesc(
                Long userId,
                Pageable pageable
        );

        Page<PseudoTestAttempt> findByUserIdAndSkill_SlugOrderByCreatedAtDesc(
                Long userId,
                String skillSlug,
                Pageable pageable
        );

        @Query("""
            SELECT new com.ja.pseudo.dto.SkillAnalyticsResponse(
                s.title,
                COUNT(a),
                AVG(a.score)
            )
            FROM PseudoTestAttempt a
            JOIN a.skill s
            WHERE a.completed = true
            GROUP BY s.title
        """)
        List<SkillAnalyticsResponse> getSkillAnalytics();

        @Query("""
            SELECT new com.ja.pseudo.dto.SubscriptionAnalyticsResponse(
                u.subscription,
                COUNT(a)
            )
            FROM PseudoTestAttempt a
            JOIN User u ON a.userId = u.id
            GROUP BY u.subscription
        """)
        List<SubscriptionAnalyticsResponse> getSubscriptionStats();

        Optional<PseudoTestAttempt>
        findTopByUserIdAndCompletedFalseOrderByStartedAtDesc(Long userId);

        Optional<PseudoTestAttempt> findByIdAndUserId(Long id, Long userId);

        Optional<PseudoTestAttempt>
        findTopByUserIdAndSkill_SlugAndCompletedFalseOrderByStartedAtDesc(
                Long userId,
                String skillSlug
        );
    }
