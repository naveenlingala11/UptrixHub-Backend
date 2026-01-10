package com.ja.challenge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LeaderboardRow {

    private int rank;
    private Long userId;
    private int totalXp;
    private int currentStreak;
}
