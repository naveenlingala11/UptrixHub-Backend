package com.ja.pseudo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class StartTestResponse {

    private Long attemptId;
    private LocalDateTime startedAt;
    private LocalDateTime expiresAt;
    private int durationMinutes;
}

