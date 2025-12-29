package com.ja.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Configuration
public class RedisWebSocketConfig {

    @Bean(name = "wsRedisContainer")
    @DependsOn("redisConnectionFactory")
    public RedisMessageListenerContainer wsRedisContainer(
            RedisConnectionFactory factory,
            WebSocketEventSubscriber subscriber
    ) {
        RedisMessageListenerContainer container =
                new RedisMessageListenerContainer();

        container.setConnectionFactory(factory);
        container.addMessageListener(
                subscriber,
                new ChannelTopic("ws-events")
        );

        return container;
    }
}
