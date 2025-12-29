package com.ja.admin.events;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.*;

@Configuration
public class RedisAdminConfig {

    @Bean(name = "adminRedisContainer")
    public RedisMessageListenerContainer adminRedisContainer(
            RedisConnectionFactory factory,
            AdminEventSubscriber subscriber
    ) {
        RedisMessageListenerContainer container =
                new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);
        container.addMessageListener(
                subscriber,
                new ChannelTopic("admin-events")
        );
        return container;
    }
}
