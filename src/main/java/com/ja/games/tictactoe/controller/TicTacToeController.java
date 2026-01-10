package com.ja.games.tictactoe.controller;


import com.ja.games.tictactoe.entity.TicTacToeStats;
import com.ja.games.tictactoe.repository.TicTacToeStatsRepository;
import com.ja.games.tictactoe.service.TicTacToeService;
import com.ja.security.JwtUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/games/tic-tac-toe")
@RequiredArgsConstructor
public class TicTacToeController {

    private final TicTacToeService service;

    @PostMapping("/result")
    public Map<String, Object> submitResult(
            @AuthenticationPrincipal JwtUserPrincipal user,
            @RequestBody Map<String, String> req
    ) {
        if (user == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "User not authenticated"
            );
        }

        int xp = service.handleResult(
                user.getUserId(),
                req.get("result")
        );

        return Map.of("earnedXp", xp);
    }

    @GetMapping("/leaderboard")
    public List<TicTacToeStats> leaderboard(
            TicTacToeStatsRepository repo
    ) {
        return repo.findAll(
                Sort.by(Sort.Direction.DESC, "wins")
        );
    }
}
