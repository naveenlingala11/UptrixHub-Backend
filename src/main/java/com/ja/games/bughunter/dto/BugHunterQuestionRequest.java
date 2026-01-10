package com.ja.games.bughunter.dto;

import lombok.Data;

@Data
public class BugHunterQuestionRequest {

    private String language;     // JAVA
    private String title;
    private String difficulty;   // EASY / MEDIUM / HARD
    private String code;
    private String bugType;      // Compile-time Error / Runtime Exception / Logical Bug
    private String reason;
    private String fix;
    private int xp;
}
