package com.ja.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.Map;
import java.util.List;

@Data
@AllArgsConstructor
public class AdminAnalyticsResponse {

    private Map<LocalDate, Integer> dailyActiveUsers;
    private Map<LocalDate, Integer> xpGained;
    private Map<LocalDate, Integer> gameAttempts;

    private List<TopUserXp> topUsers;
}
