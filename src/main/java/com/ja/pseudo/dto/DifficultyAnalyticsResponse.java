package com.ja.pseudo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DifficultyAnalyticsResponse {

    private String difficulty;
    private Long count;
}
