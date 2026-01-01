package com.ja.pseudo.dto;

import com.ja.pseudo.entity.PseudoTestAttempt;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserAttemptResponse {

    private Long attemptId;
    private String skill;
    private int totalQuestions;
    private int correct;
    private int wrong;
    private int score;
    private int accuracy;
    private boolean completed;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;

    public static UserAttemptResponse from(PseudoTestAttempt a) {

        int accuracy = a.getTotalQuestions() == 0
                ? 0
                : (a.getCorrectAnswers() * 100) / a.getTotalQuestions();

        return new UserAttemptResponse(
                a.getId(),
                a.getSkill().getTitle(),
                a.getTotalQuestions(),
                a.getCorrectAnswers(),
                a.getWrongAnswers(),
                a.getScore(),
                accuracy,
                a.isCompleted(),
                a.getStartedAt(),
                a.getCreatedAt()
        );
    }
}

