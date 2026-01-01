package com.ja.pseudo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "pseudo_test_violation")
@Getter
@Setter
public class PseudoTestViolation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long attemptId;

    private String type; // TAB_SWITCH, BLUR, FULLSCREEN_EXIT

    private LocalDateTime createdAt = LocalDateTime.now();
}

