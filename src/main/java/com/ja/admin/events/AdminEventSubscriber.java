package com.ja.admin.events;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class AdminEventSubscriber implements MessageListener {

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String payload = new String(message.getBody());
        System.out.println("🔥 ADMIN EVENT RECEIVED: " + payload);
    }
}
