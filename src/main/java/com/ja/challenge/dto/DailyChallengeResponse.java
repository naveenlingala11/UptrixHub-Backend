package com.ja.challenge.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DailyChallengeResponse {

    private boolean success;
    private boolean locked;

    private String verdict;
    private String message;

    private int xpEarned;
}
