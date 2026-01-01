package com.ja.pseudo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AnswerSubmitResponse {

    private boolean correct;
    private String summary;
    private List<String> steps;
    private List<String> concepts;
}

