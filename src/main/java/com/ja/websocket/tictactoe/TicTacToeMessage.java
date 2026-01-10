package com.ja.websocket.tictactoe;

import lombok.Data;

@Data
public class TicTacToeMessage {
    private String type;   // JOIN, MOVE, MATCH
    private String roomId;
    private int index;
}
