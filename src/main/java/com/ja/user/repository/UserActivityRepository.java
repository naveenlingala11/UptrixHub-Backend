package com.ja.user.repository;

import com.ja.user.entity.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserActivityRepository
        extends JpaRepository<UserActivity, Long> {

    List<UserActivity> findTop10ByUserIdOrderByCreatedAtDesc(Long userId);
}
