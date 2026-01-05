package com.ja.games.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "xp_rule",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"gameType", "action"}
        ))
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class XpRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String gameType;   // BUG_HUNTER, PSEUDO_CODE
    private String action;     // SOLVE, CORRECT, STREAK_BONUS

    private Integer xp;        // XP to award
    private boolean enabled = true;
}
