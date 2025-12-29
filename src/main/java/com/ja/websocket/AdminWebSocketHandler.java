package com.ja.websocket;

import com.ja.security.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.URI;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@RequiredArgsConstructor
public class AdminWebSocketHandler extends TextWebSocketHandler {

    private final JwtUtil jwtUtil;

    private static final Set<WebSocketSession> sessions =
            new CopyOnWriteArraySet<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        URI uri = session.getUri();
        if (uri == null || uri.getQuery() == null) {
            session.close();
            return;
        }

        String token = uri.getQuery().replace("token=", "");

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtUtil.getSigningKey()) // expose this method
                    .build()
                    .parseClaimsJws(token)
                    .getBody();


            String role = claims.get("role", String.class);
            if (!"ADMIN".equals(role)) {
                session.close();
                return;
            }

            sessions.add(session);
            System.out.println("✅ Admin WS connected: " + session.getId());

        } catch (Exception e) {
            session.close();
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message)
            throws Exception {

        if ("PING".equals(message.getPayload())) {
            session.sendMessage(new TextMessage("PONG"));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        System.out.println("❌ Admin WS disconnected: " + session.getId());
    }

    /* 🔔 BROADCAST */
    public static void broadcast(String json) {
        for (WebSocketSession session : sessions) {
            try {
                session.sendMessage(new TextMessage(json));
            } catch (Exception ignored) {}
        }
    }
}
