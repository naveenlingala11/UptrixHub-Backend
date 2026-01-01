package com.ja.pseudo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "pseudo_test_answer",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"attempt_id", "question_id"}))
@Getter
@Setter
public class PseudoTestAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long attemptId;
    private Long questionId;
    private Character selectedOption;

    private LocalDateTime updatedAt = LocalDateTime.now();
}

