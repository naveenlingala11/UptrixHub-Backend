package com.ja.home.kit.repository;

import com.ja.home.kit.entity.UserKitProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserKitProgressRepository
        extends JpaRepository<UserKitProgress, Long> {

    List<UserKitProgress> findByUserId(Long userId);
}
