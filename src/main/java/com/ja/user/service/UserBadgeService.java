package com.ja.user.service;

import com.ja.user.entity.User;
import com.ja.user.entity.UserBadge;
import com.ja.user.repository.UserBadgeRepository;
import com.ja.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UserBadgeService {

    private final UserRepository userRepo;
    private final UserBadgeRepository badgeRepo;

    public void awardBadge(Long userId, String badgeName, String icon) {

        User user = userRepo.findById(userId).orElseThrow();

        boolean alreadyHas =
                badgeRepo.existsByUserIdAndName(userId, badgeName);

        if (alreadyHas) return;

        UserBadge badge = new UserBadge();
        badge.setName(badgeName);
        badge.setIcon(icon);
        badge.setEarnedAt(LocalDate.now());
        badge.setUser(user);

        badgeRepo.save(badge);
    }
}
