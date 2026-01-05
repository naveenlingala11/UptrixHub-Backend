package com.ja.games.bughunter.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "bug_hunter_attempt")
@Getter
@Setter
public class BugHunterAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long questionId;

    private boolean correct;

    private String selectedAnswer;

    private String bugCategory;

    private int earnedXp;

    private LocalDateTime attemptedAt = LocalDateTime.now();
}
