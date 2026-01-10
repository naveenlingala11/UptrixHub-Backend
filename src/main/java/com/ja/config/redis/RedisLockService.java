package com.ja.config.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisLockService {

    private final StringRedisTemplate redis;

    public boolean lock(String key, int seconds) {
        return Boolean.TRUE.equals(
                redis.opsForValue()
                        .setIfAbsent(key, "LOCKED", Duration.ofSeconds(seconds))
        );
    }

    public void unlock(String key) {
        redis.delete(key);
    }
}
