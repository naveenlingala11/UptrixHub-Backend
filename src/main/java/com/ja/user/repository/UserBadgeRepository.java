package com.ja.user.repository;

import com.ja.user.entity.User;
import com.ja.user.entity.UserBadge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserBadgeRepository
        extends JpaRepository<UserBadge, Long> {

    List<UserBadge> findByUser_Id(Long userId);

    boolean existsByUserIdAndName(Long userId, String name);


}
