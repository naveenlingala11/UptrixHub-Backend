package com.ja.pseudo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WeakQuestionResponse {

    private Long questionId;
    private String skill;
    private Long wrongCount;
}
