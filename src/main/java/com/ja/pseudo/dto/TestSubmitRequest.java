package com.ja.pseudo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class TestSubmitRequest {

    private String skill; // core-java

    // questionId -> selected option
    private Map<Long, Character> answers;
}

