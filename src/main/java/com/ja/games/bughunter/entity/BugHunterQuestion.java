package com.ja.games.bughunter.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "bug_hunter_question")
@Getter @Setter
public class BugHunterQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long parentId;

    @Column(nullable = false)
    private Integer version = 1;

    private String title;
    private String language;
    private String difficulty;

    @Column(columnDefinition = "TEXT")
    private String code;

    private String bugCategory; // Runtime Exception / Logical Bug / Compile-time Error
    private String bugType;     // NullPointerException / Deadlock / etc

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Column(columnDefinition = "TEXT")
    private String fix;

    private Integer xp;

    @Column(nullable = false)
    private boolean published = false;

    @Column(nullable = false)
    private boolean active = true;

    private LocalDateTime createdAt = LocalDateTime.now();
}
