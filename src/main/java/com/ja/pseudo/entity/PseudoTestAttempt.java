package com.ja.pseudo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "pseudo_test_attempt")
@Getter @Setter
public class PseudoTestAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "skill_id")
    private PseudoSkill skill;

    private Long userId;

    private int totalQuestions;
    private int correctAnswers;
    private int wrongAnswers;
    private int score;

    private LocalDateTime startedAt;
    private LocalDateTime expiresAt;
    private boolean completed = false;

    private LocalDateTime createdAt = LocalDateTime.now();
}
