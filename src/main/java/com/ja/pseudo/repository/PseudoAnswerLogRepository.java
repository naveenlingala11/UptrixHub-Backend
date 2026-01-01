package com.ja.pseudo.repository;

import com.ja.pseudo.dto.WeakQuestionResponse;
import com.ja.pseudo.entity.PseudoAnswerLog;
import com.ja.pseudo.entity.PseudoQuestion;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PseudoAnswerLogRepository
        extends JpaRepository<PseudoAnswerLog, Long> {

    @Query("""
        SELECT new com.ja.pseudo.dto.WeakQuestionResponse(
            q.id,
            s.title,
            COUNT(l)
        )
        FROM PseudoAnswerLog l
        JOIN PseudoQuestion q ON l.questionId = q.id
        JOIN q.skill s
        WHERE l.correct = false
        GROUP BY q.id, s.title
        ORDER BY COUNT(l) DESC
    """)
    Page<WeakQuestionResponse> getMostFailedQuestions(Pageable pageable);
}
