package com.ja.pseudo.repository;

import com.ja.pseudo.entity.PseudoTestAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PseudoTestAnswerRepository
        extends JpaRepository<PseudoTestAnswer, Long> {

    Optional<PseudoTestAnswer> findByAttemptIdAndQuestionId(
            Long attemptId,
            Long questionId
    );

    List<PseudoTestAnswer> findByAttemptId(Long attemptId);
}

