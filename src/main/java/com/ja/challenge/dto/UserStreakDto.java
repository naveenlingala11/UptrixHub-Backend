package com.ja.challenge.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserStreakDto {

    private int currentStreak;
    private int longestStreak;

    private boolean streak7;
    private boolean streak14;
    private boolean streak30;

    private int totalXp;
}
