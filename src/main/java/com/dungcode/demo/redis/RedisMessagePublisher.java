/**
 * Copyright (c) 2025 dungduong
 * Sản phẩm: LocaAI Glasses
 *
 * @author dungduong
 * @version 1.0
 * @since 21/07/2025
 */


package com.dungcode.demo.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

@Slf4j
public class RedisMessagePublisher implements MessagePublisher {

    private final RedisTemplate<String, Object> redisTemplate;
    private final String channel;

    public RedisMessagePublisher(RedisTemplate<String, Object> redisTemplate, String channel) {
        this.redisTemplate = redisTemplate;
        this.channel = channel;
    }

    @Override
    public void publish(String message) {
        if (message == null || message.isEmpty()) {
            throw new IllegalArgumentException("Message cannot be null or empty");
        }
        log.info("Channel: {}", channel);
        redisTemplate.convertAndSend(channel, message);
    }
}
