package com.ja.config.redis;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Profile("prod")
public class RedisHealthCheck {

    private final RedisTemplate<String, Object> redis;

    @PostConstruct
    public void check() {
        redis.opsForValue().set("health:redis", "OK");
        System.out.println("🔥 Redis Connected Successfully");
    }
}
