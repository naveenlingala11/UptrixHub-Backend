package com.ja.challenge.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DailyChallengeView {

    private String title;
    private String difficulty;
    private String time;

    private List<String> tags;
    private List<String> problem;

    private String starterCode;

    private boolean locked;
    private Integer xpEarned;
}
