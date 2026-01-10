package com.ja.websocket;

import com.ja.websocket.tictactoe.TicTacToeWebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final AdminWebSocketHandler adminWebSocketHandler;
    private final TicTacToeWebSocketHandler ticTacToeWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

        registry
                .addHandler(adminWebSocketHandler, "/ws/admin")
                .setAllowedOrigins("*");

        registry
                .addHandler(ticTacToeWebSocketHandler, "/ws/tic-tac-toe")
                .setAllowedOrigins("*");
    }
}
