package com.ja.pseudo.dto;

import com.ja.pseudo.entity.PseudoQuestion;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PseudoQuestionResponse {

    private Long id;
    private String question;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;

    public static PseudoQuestionResponse from(PseudoQuestion q) {
        return new PseudoQuestionResponse(
                q.getId(),
                q.getQuestion(),
                q.getOptionA(),
                q.getOptionB(),
                q.getOptionC(),
                q.getOptionD()
        );
    }
}
