package com.ja.features.mock.interview.repository;

import com.ja.features.mock.interview.entity.MockInterviewSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MockInterviewSessionRepository
        extends JpaRepository<MockInterviewSession, Long> {

    Optional<MockInterviewSession> findByRequestId(Long requestId);

    List<MockInterviewSession> findByRequestIdIn(List<Long> requestIds);

    Optional<MockInterviewSession> findTopByRequestIdOrderByCreatedAtDesc(Long requestId);

    @Query("""
    SELECT DISTINCT s
    FROM MockInterviewSession s
    LEFT JOIN FETCH s.strengths
    LEFT JOIN FETCH s.weaknesses
    WHERE s.requestId IN :requestIds
""")
    List<MockInterviewSession> findWithDetailsByRequestIds(
            @Param("requestIds") List<Long> requestIds
    );

}