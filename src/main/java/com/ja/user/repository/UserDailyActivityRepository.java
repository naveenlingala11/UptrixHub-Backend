package com.ja.user.repository;

import com.ja.user.entity.UserDailyActivity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface UserDailyActivityRepository
        extends JpaRepository<UserDailyActivity, Long> {

    List<UserDailyActivity>
    findByUser_IdAndDateAfter(Long userId, LocalDate date);
}
