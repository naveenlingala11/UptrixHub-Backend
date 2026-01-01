package com.ja.pseudo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class QuestionUploadResponse {

    private int total;
    private int success;
    private int failed;
    private List<String> errors;
}

