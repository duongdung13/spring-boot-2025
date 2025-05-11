package com.dungcode.demo.service;

import com.dungcode.demo.config.RabbitMQConfig;
import com.dungcode.demo.posgresql.entity.OrderEvent;
import com.dungcode.demo.posgresql.entity.NotificationEvent;
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

    public void sendOrderEvent(OrderEvent orderEvent) {
        log.info("Sending order event to exchange: {}", orderEvent);

        // Send to inventory service
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.INVENTORY_EXCHANGE,
                RabbitMQConfig.ORDER_TO_INVENTORY_ROUTING_KEY,
                orderEvent
        );

        // Create and send notification event
        NotificationEvent notificationEvent = new NotificationEvent(
                UUID.randomUUID().toString(),
                "EMAIL",
                orderEvent.getCustomerId(),
                "Order " + orderEvent.getEventType(),
                "Your order has been " + orderEvent.getEventType().toLowerCase(),
                new Date(),
                NotificationEvent.NotificationStatus.PENDING
        );

        // Send to notification service
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.NOTIFICATION_EXCHANGE,
                RabbitMQConfig.ORDER_TO_NOTIFICATION_ROUTING_KEY,
                notificationEvent
        );
    }

//    public void sendInventoryEvent(InventoryEvent inventoryEvent) {
//        log.info("Sending inventory event: {}", inventoryEvent);
//        rabbitTemplate.convertAndSend(
//                RabbitMQConfig.ORDER_EXCHANGE,
//                RabbitMQConfig.ORDER_TO_INVENTORY_ROUTING_KEY,
//                inventoryEvent
//        );
//    }
//
//    public void sendNotificationEvent(NotificationEvent notificationEvent) {
//        log.info("Sending notification event: {}", notificationEvent);
//        rabbitTemplate.convertAndSend(
//                RabbitMQConfig.NOTIFICATION_EXCHANGE,
//                RabbitMQConfig.ORDER_TO_NOTIFICATION_ROUTING_KEY,
//                notificationEvent
//        );
//    }
} 