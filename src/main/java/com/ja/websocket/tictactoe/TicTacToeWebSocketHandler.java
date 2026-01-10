package com.ja.websocket.tictactoe;

import com.ja.games.tictactoe.service.TicTacToeXpService;
import com.ja.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.ja.websocket.util.JsonUtil;

import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
@RequiredArgsConstructor
public class TicTacToeWebSocketHandler extends TextWebSocketHandler {

    private final JwtUtil jwtUtil;
    private final TicTacToeXpService ticTacToeXpService;

    private static final Map<String, TicTacToeGame> games =
            new ConcurrentHashMap<>();

    private static final Map<WebSocketSession, String> sessions =
            new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        System.out.println("🎮 TicTacToe WS connected: " + session.getId());
    }

    @Override
    protected void handleTextMessage(
            @NonNull WebSocketSession session,
            @NonNull TextMessage msg
    ) throws Exception {

        TicTacToeMessage m =
                JsonUtil.fromJson(msg.getPayload(), TicTacToeMessage.class);

        Long userId = extractUserId(session);

        if ("JOIN".equals(m.getType())) {
            joinRoom(session, userId, m.getRoomId());
        }

        if ("MOVE".equals(m.getType())) {
            makeMove(session, userId, m);
        }

        if ("MATCH".equals(m.getType())) {
            handleMatchmaking(session, userId);
        }

    }

    private void joinRoom(
            WebSocketSession session,
            Long userId,
            String roomId
    ) throws Exception {

        TicTacToeGame game =
                games.computeIfAbsent(roomId, id -> new TicTacToeGame());

        if (game.getPlayerX() == null) {
            game.setPlayerX(userId);
        } else if (game.getPlayerO() == null) {
            game.setPlayerO(userId);
        } else {
            session.close();
            return;
        }

        sessions.put(session, roomId);
        broadcast(roomId, game);
    }

    private void handleMatchmaking(
            WebSocketSession session,
            Long userId
    ) throws Exception {

        // If someone already waiting → pair
        WebSocketSession opponent = waitingQueue.poll();

        if (opponent == null || !opponent.isOpen()) {
            waitingQueue.add(session);
            session.sendMessage(
                    new TextMessage("{\"type\":\"WAITING\"}")
            );
            return;
        }

        // Create room
        String roomId = "room-" + UUID.randomUUID();

        TicTacToeGame game = new TicTacToeGame();
        game.setPlayerX(userId);
        game.setPlayerO(extractUserId(opponent));

        games.put(roomId, game);

        sessions.put(session, roomId);
        sessions.put(opponent, roomId);

        // Notify both players
        String msg = JsonUtil.toJson(
                Map.of("type", "MATCH", "roomId", roomId)
        );

        session.sendMessage(new TextMessage(msg));
        opponent.sendMessage(new TextMessage(msg));

        // Send initial game state
        broadcast(roomId, game);
    }

    @SuppressWarnings("unused")
    private void makeMove(
            WebSocketSession session,
            Long userId,
            TicTacToeMessage m
    ) throws Exception {

        TicTacToeGame game = games.get(m.getRoomId());
        if (game == null || game.isFinished()) return;

        String symbol =
                userId.equals(game.getPlayerX()) ? "X" :
                        userId.equals(game.getPlayerO()) ? "O" : null;

        if (symbol == null || !symbol.equals(game.getTurn())) return;
        if (!game.getBoard()[m.getIndex()].isEmpty()) return;

        game.getBoard()[m.getIndex()] = symbol;
        game.setTurn(symbol.equals("X") ? "O" : "X");

        if (checkWin(game.getBoard(), symbol)) {

            game.setFinished(true);

            Long winnerId =
                    symbol.equals("X") ? game.getPlayerX()
                            : game.getPlayerO();

            Long loserId =
                    symbol.equals("X") ? game.getPlayerO()
                            : game.getPlayerX();

            // 🔥 XP + ACHIEVEMENT
            ticTacToeXpService.handleOnlineWin(winnerId);

            // (optional) record loss
        }


        broadcast(m.getRoomId(), game);
    }

    private boolean checkWin(String[] b, String p) {
        int[][] w = {
                {0,1,2},{3,4,5},{6,7,8},
                {0,3,6},{1,4,7},{2,5,8},
                {0,4,8},{2,4,6}
        };
        for (int[] x : w)
            if (p.equals(b[x[0]]) &&
                    p.equals(b[x[1]]) &&
                    p.equals(b[x[2]]))
                return true;
        return false;
    }

    private void broadcast(String roomId, TicTacToeGame game)
            throws Exception {

        String json = JsonUtil.toJson(game);

        for (var e : sessions.entrySet()) {
            if (roomId.equals(e.getValue())) {
                e.getKey().sendMessage(new TextMessage(json));
            }
        }
    }

    // 🔥 MATCHMAKING QUEUE
    private static final Queue<WebSocketSession> waitingQueue =
            new ConcurrentLinkedQueue<>();

    private Long extractUserId(WebSocketSession session) {

        if (session.getUri() == null ||
                session.getUri().getQuery() == null) {
            throw new IllegalStateException("Missing token");
        }

        String query = session.getUri().getQuery();
        String token = query.replace("token=", "");

        return jwtUtil.extractUserId(token);
    }

}
