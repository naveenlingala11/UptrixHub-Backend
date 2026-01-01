package com.ja.pseudo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerSubmitRequest {

    private Long questionId;

    // A / B / C / D
    private char selectedOption;
}

