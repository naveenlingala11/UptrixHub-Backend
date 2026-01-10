package com.ja.challenge.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserStats {

    @Id
    private Long userId;

    private int totalXp;

    private int currentStreak;

    private int maxStreak;

    private String lastSolvedDate; // yyyy-MM-dd
}
