package com.ja.games.bughunter.ai.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "bug_ai_explanation")
@Getter
@Setter
public class BugAiExplanation {

    @Id
    @GeneratedValue
    private Long id;

    private Long questionId;
    private String bugType;
    private String difficulty;

    @Column(length = 4000)
    private String explanation;

    private LocalDateTime generatedAt;
}
