package com.ja.user.service;

import com.ja.admin.events.AdminEventPublisher;
import com.ja.user.dto.*;
import com.ja.user.dto.UserPublicProfileResponse;
import com.ja.user.entity.*;
import com.ja.user.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepo;
    private final UserSkillRepository skillRepo;
    private final UserAchievementRepository achievementRepo;
    private final UserBadgeRepository badgeRepo;
    private final UserActivityRepository activityRepo;
    private final UserDailyActivityRepository dailyActivityRepo;


    // ✅ EXISTING
    private final AdminEventPublisher adminEventPublisher;

    /* =========================
       UPDATE PROFILE (SAFE)
    ========================== */
    public void updateProfile(Long userId, UpdateProfileRequest req) {

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));

        // 🔍 TRACK NAME CHANGE
        if (req.getName() != null &&
                !req.getName().isBlank() &&
                !req.getName().equals(user.getName())) {

            activityRepo.save(new UserActivity(
                    "NAME_CHANGED",
                    "Name changed from " + user.getName(),
                    user
            ));

            user.setName(req.getName().trim());
        }

        if (req.getAvatar() != null && !req.getAvatar().isBlank()) {
            user.setAvatar(req.getAvatar());
        }

        if (req.getMobile() != null && !req.getMobile().isBlank()) {
            user.setMobile(req.getMobile());
        }

        userRepo.save(user);

        // 🔔 ACTIVITY
        activityRepo.save(new UserActivity(
                "PROFILE_UPDATED",
                "Profile updated",
                user
        ));

        // 🔔 ADMIN EVENT (UNCHANGED)
        adminEventPublisher.publish("""
        {
          "type": "PROFILE_UPDATED",
          "message": "Profile updated: %s"
        }
        """.formatted(user.getEmail()));
    }

    /* =========================
       GET PROFILE (DYNAMIC)
    ========================== */
    public UserProfileResponse getProfile(Long userId) {

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));

        List<UserSkill> skills = skillRepo.findByUser_Id(userId);
        List<UserAchievement> achievements = achievementRepo.findByUserId(userId);
        List<UserBadge> badges = badgeRepo.findByUser_Id(userId);
        List<UserActivity> activity =
                activityRepo.findTop10ByUserIdOrderByCreatedAtDesc(userId);

        return new UserProfileResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getMobile(),
                user.getAvatar(),
                user.getBio(),
                user.getProvider() != null ? user.getProvider().name() : "LOCAL",
                user.getSubscription() != null ? user.getSubscription().name() : "FREE",

                skills.stream().map(UserSkillResponse::from).toList(),
                achievements.stream().map(UserAchievementResponse::from).toList(),
                badges.stream().map(UserBadgeResponse::from).toList(),
                activity.stream().map(UserActivityResponse::from).toList(),

                new ProfileStats(
                        skills.size(),
                        achievements.size(),
                        badges.size()
                )
        );
    }
    @Transactional
    public UserPublicProfileResponse getPublicProfileById(Long userId) {

        User user = userRepo.findById(userId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Public profile not found"
                        )
                );

        if (user.isDeleted()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Public profile not found"
            );
        }

        List<UserSkillResponse> skills =
                skillRepo.findByUser_Id(userId)
                        .stream()
                        .map(UserSkillResponse::from)
                        .toList();

        List<UserBadgeResponse> badges =
                badgeRepo.findByUser_Id(userId)
                        .stream()
                        .map(UserBadgeResponse::from)
                        .toList();

        return new UserPublicProfileResponse(
                user.getId(),
                user.getName(),
                user.getAvatar(),
                user.getBio(),
                skills,
                badges
        );
    }
    /* =========================
       EXISTING METHOD (KEEP)
    ========================== */
    public User getUserById(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));
    }

    public Map<LocalDate, Integer> getHeatmap(Long userId) {
        return dailyActivityRepo
                .findByUser_IdAndDateAfter(
                        userId,
                        LocalDate.now().minusDays(180)
                )
                .stream()
                .collect(Collectors.toMap(
                        UserDailyActivity::getDate,
                        UserDailyActivity::getCount
                ));
    }
    @Transactional
    public void addAchievement(Long userId, String title, String description) {

        User user = getUserById(userId);

        UserAchievement ua = new UserAchievement();
        ua.setUser(user);
        ua.setTitle(title);
        ua.setDescription(description);
        ua.setAchievedAt(LocalDate.now());

        achievementRepo.save(ua);
    }

}
