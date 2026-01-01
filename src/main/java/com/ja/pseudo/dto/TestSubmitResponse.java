package com.ja.pseudo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TestSubmitResponse {

    private int totalQuestions;
    private int correct;
    private int wrong;
    private int score;
}
