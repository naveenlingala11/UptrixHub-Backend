package com.ja.challenge.dto;

import lombok.Data;
import java.util.List;

@Data
public class AdminDailyChallengeRequest {

    private String date;
    private String title;
    private String difficulty;
    private String time;

    private List<String> tags;
    private List<String> problem;

    private String starterCode;

    private List<TestCaseDto> tests;

    private int xpReward;
}
