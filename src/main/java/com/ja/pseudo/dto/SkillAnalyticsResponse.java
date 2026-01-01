package com.ja.pseudo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SkillAnalyticsResponse {

    private String skill;
    private Long attempts;
    private Double averageScore;
}
