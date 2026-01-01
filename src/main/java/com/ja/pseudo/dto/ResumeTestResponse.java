package com.ja.pseudo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@AllArgsConstructor
public class ResumeTestResponse {

    private Long attemptId;
    private LocalDateTime expiresAt;
    private Map<Long, Character> answers;
}

