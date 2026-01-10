package com.ja.games.tictactoe.repository;

import com.ja.games.tictactoe.entity.TicTacToeStats;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicTacToeStatsRepository
        extends JpaRepository<TicTacToeStats, Long> {
}
