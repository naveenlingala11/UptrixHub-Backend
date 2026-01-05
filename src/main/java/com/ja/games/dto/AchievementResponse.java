package com.ja.games.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AchievementResponse {
    private String code;
    private String title;
    private String description;
    private String icon;
    private boolean unlocked;
}
