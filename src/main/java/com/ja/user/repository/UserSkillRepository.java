package com.ja.user.repository;

import com.ja.user.entity.User;
import com.ja.user.entity.UserSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserSkillRepository extends JpaRepository<UserSkill, Long> {
    List<UserSkill> findByUser_Id(Long userId);
    List<UserSkill> findByUser(User user);

}
