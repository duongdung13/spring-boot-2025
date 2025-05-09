package com.dungcode.demo.service;

import com.dungcode.demo.config.RabbitMQConfig;
import com.dungcode.demo.model.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitMQProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(String content) {
        Message message = new Message(
                UUID.randomUUID().toString(),
                content,
                new Date()
        );
        
        log.info("Sending message to queue: {}", message);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY,
                message
        );
    }
} 