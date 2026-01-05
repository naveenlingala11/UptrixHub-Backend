package com.ja.games.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubmitBugAnswerRequest {
    private Long userId;
    private Long questionId;
    private String selectedAnswer;
}

