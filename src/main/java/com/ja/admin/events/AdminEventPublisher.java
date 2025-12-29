package com.ja.admin.events;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class AdminEventPublisher {

    private final StringRedisTemplate redisTemplate;

    public AdminEventPublisher(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void publish(String json) {
        redisTemplate.convertAndSend("admin-events", json);
    }
}
