package com.ja.pseudo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "pseudo_question")
@Getter
@Setter
public class PseudoQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "skill_id")
    private PseudoSkill skill;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String question;

    @Column(name = "option_a", nullable = false)
    private String optionA;

    @Column(name = "option_b", nullable = false)
    private String optionB;

    @Column(name = "option_c", nullable = false)
    private String optionC;

    @Column(name = "option_d", nullable = false)
    private String optionD;

    @Column(name = "correct_option", nullable = false)
    private char correctOption;

    @Column(nullable = false)
    private String difficulty;

    @Column(name = "access_level", nullable = false)
    private String accessLevel;

    // OLD (KEEP)
    @Column(columnDefinition = "TEXT")
    private String explanation;

    // NEW (JSON STORED AS TEXT)
    @Column(name = "explanation_summary", columnDefinition = "TEXT")
    private String explanationSummary;

    @Column(name = "explanation_steps", columnDefinition = "TEXT")
    private String explanationStepsJson;

    @Column(name = "explanation_concepts", columnDefinition = "TEXT")
    private String explanationConceptsJson;
}

