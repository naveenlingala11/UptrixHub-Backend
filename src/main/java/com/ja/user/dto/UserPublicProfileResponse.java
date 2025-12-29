package com.ja.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserPublicProfileResponse {

    private Long id;
    private String name;
    private String avatar;
    private String bio;

    private List<UserSkillResponse> skills;
    private List<UserBadgeResponse> badges;
}
