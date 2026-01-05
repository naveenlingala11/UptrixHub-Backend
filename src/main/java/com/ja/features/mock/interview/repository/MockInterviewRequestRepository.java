package com.ja.features.mock.interview.repository;

import com.ja.features.mock.interview.entity.MockInterviewRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MockInterviewRequestRepository
        extends JpaRepository<MockInterviewRequest, Long> {

    List<MockInterviewRequest> findByUserId(Long userId);

    List<MockInterviewRequest> findByStatus(String status);

    List<MockInterviewRequest> findByUserIdOrderByCreatedAtDesc(Long userId);

    @Query("""
    SELECT DISTINCT r
    FROM MockInterviewRequest r
    LEFT JOIN FETCH r.preferredSlots
    WHERE r.userId = :userId
    ORDER BY r.createdAt DESC
""")
    List<MockInterviewRequest> findWithSlotsByUserId(@Param("userId") Long userId);

}
