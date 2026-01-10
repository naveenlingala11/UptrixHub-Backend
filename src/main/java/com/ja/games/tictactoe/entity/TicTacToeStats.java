package com.ja.games.tictactoe.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tic_tac_toe_stats")
@Getter @Setter
public class TicTacToeStats {

    @Id
    private Long userId;

    private int wins;
    private int losses;
    private int draws;

    // 🔥 NEW
    private int onlineWins;
}
