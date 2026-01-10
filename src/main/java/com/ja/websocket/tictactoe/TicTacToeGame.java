package com.ja.websocket.tictactoe;

import lombok.Data;

@Data
public class TicTacToeGame {

    private String[] board = new String[9];
    private Long playerX;
    private Long playerO;
    private String turn = "X";
    private boolean finished = false;

    public TicTacToeGame() {
        for (int i = 0; i < 9; i++) board[i] = "";
    }
}
