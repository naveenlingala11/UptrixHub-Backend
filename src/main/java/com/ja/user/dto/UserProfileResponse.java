package com.ja.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserProfileResponse {

    private Long id;
    private String name;
    private String email;
    private String mobile;
    private String avatar;
    private String bio;
    private String provider;
    private String subscription;

    private List<UserSkillResponse> skills;
    private List<UserAchievementResponse> achievements;
    private List<UserBadgeResponse> badges;
    private List<UserActivityResponse> activity;

    private ProfileStats stats;
}
