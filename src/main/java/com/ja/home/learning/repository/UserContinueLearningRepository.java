package com.ja.home.learning.repository;

import com.ja.home.learning.entity.UserContinueLearning;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserContinueLearningRepository
        extends JpaRepository<UserContinueLearning, Long> {

    List<UserContinueLearning> findAllByUserId(Long userId);
}

