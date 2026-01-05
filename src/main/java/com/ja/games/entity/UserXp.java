package com.ja.games.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "user_xp")
@Getter
@Setter
public class UserXp {

    @Id
    private Long userId;

    private int totalXp;
    private int level;

    private int currentStreak;
    private int longestStreak;

    private LocalDate lastPlayed;
}
