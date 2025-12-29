package com.ja.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class WebSocketEventSubscriber implements MessageListener {

    private static final Logger log =
            LoggerFactory.getLogger(WebSocketEventSubscriber.class);

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String payload = new String(message.getBody());
        log.info("📡 WS EVENT RECEIVED: {}", payload);

        // TODO: forward this to WebSocket sessions
        // websocketService.broadcast(payload);
    }
}
