package com.ja.pseudo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "pseudo_answer_log")
@Getter
@Setter
public class PseudoAnswerLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "question_id", nullable = false)
    private Long questionId;

    @Column(nullable = false)
    private boolean correct;

    private LocalDateTime createdAt = LocalDateTime.now();
}
