package com.ja.pseudo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PseudoQuestionUploadDto {

    private String question;
    private List<String> options;   // size = 4
    private String correct;          // "A" | "B" | "C" | "D"
    private String difficulty;       // EASY | MEDIUM | HARD
    private String access;           // FREE | PRO
    private String explanation;
}
