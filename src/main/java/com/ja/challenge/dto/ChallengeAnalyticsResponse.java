package com.ja.challenge.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChallengeAnalyticsResponse {

    private long totalSubmissions;
    private long successfulSubmissions;
    private double successRate;
    private double averageXp;
}
