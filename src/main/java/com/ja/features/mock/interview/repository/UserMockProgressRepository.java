package com.ja.features.mock.interview.repository;

import com.ja.features.mock.interview.entity.UserMockProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserMockProgressRepository
        extends JpaRepository<UserMockProgress, Long> {

    Optional<UserMockProgress> findByUserId(Long userId);
}
