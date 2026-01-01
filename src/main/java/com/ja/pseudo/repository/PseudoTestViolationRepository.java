package com.ja.pseudo.repository;

import com.ja.pseudo.entity.PseudoTestViolation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PseudoTestViolationRepository
        extends JpaRepository<PseudoTestViolation, Long> {

    long countByAttemptId(Long attemptId);
}

