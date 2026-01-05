package com.ja.games.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "xp_transaction")
@Getter
@Setter
public class XpTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String source;
    private Long sourceId;

    private int xpEarned;
    private String reason;
}
