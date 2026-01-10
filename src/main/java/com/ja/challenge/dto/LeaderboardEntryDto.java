package com.ja.challenge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LeaderboardEntryDto {

    private Long userId;
    private int score;
    private int rank;
}
