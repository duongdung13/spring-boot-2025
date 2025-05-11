package com.dungcode.demo.service;

import com.dungcode.demo.config.RabbitMQConfig;
import com.dungcode.demo.posgresql.entity.OrderEvent;
import com.dungcode.demo.posgresql.entity.NotificationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RabbitMQConsumer {

    @RabbitListener(queues = RabbitMQConfig.INVENTORY_QUEUE)
    public void handleInventoryEvent(OrderEvent orderEvent) {
        log.info("Received order event in inventory queue: {}", orderEvent);
        // Process inventory update
        // 1. Check stock availability
        // 2. Update inventory
        // 3. Create inventory event
    }

    @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_QUEUE)
    public void handleNotificationEvent(NotificationEvent notificationEvent) {
        log.info("Received notification event: {}", notificationEvent);
        // Process notification
        // 1. Send email/SMS/push notification
        // 2. Update notification status
    }
} 