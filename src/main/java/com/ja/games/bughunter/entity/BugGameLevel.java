package com.ja.games.bughunter.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "bug_game_level")
@Getter
@Setter
public class BugGameLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String difficulty; // EASY / MEDIUM / HARD

    @Column(columnDefinition = "TEXT")
    private String description;

    private boolean active = true;
}
