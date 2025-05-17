package com.dungcode.demo.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Exchange names
    public static final String INVENTORY_EXCHANGE = "inventory.exchange";
    public static final String NOTIFICATION_EXCHANGE = "notification.exchange";
    public static final String IMAGE_PROCESSING_EXCHANGE = "image.processing.exchange";

    // Queue names
    public static final String INVENTORY_QUEUE = "inventory.queue";
    public static final String NOTIFICATION_QUEUE = "notification.queue";
    public static final String IMAGE_PROCESSING_QUEUE = "image.processing.queue";

    // Routing keys
    public static final String ORDER_TO_INVENTORY_ROUTING_KEY = "order.inventory";
    public static final String ORDER_TO_NOTIFICATION_ROUTING_KEY = "order.notification";
    public static final String IMAGE_PROCESSING_ROUTING_KEY = "image.processing";

    // Inventory Exchange
    @Bean
    public DirectExchange inventoryExchange() {
        return new DirectExchange(INVENTORY_EXCHANGE);
    }

    // Notification Exchange
    @Bean
    public DirectExchange notificationExchange() {
        return new DirectExchange(NOTIFICATION_EXCHANGE);
    }

    // Image Processing Exchange
    @Bean
    public DirectExchange imageProcessingExchange() {
        return new DirectExchange(IMAGE_PROCESSING_EXCHANGE);
    }

    // Inventory Queue
    @Bean
    public Queue inventoryQueue() {
        return new Queue(INVENTORY_QUEUE, true);
    }

    // Notification Queue
    @Bean
    public Queue notificationQueue() {
        return new Queue(NOTIFICATION_QUEUE, true);
    }

    // Image Processing Queue
    @Bean
    public Queue imageProcessingQueue() {
        return QueueBuilder.durable(IMAGE_PROCESSING_QUEUE)
                .withArgument("x-message-ttl", 60000) // 1 minute TTL
                .withArgument("x-dead-letter-exchange", "dlx.exchange") // Dead letter exchange
                .build();
    }

    // Bindings
    @Bean
    public Binding inventoryBinding(Queue inventoryQueue, DirectExchange inventoryExchange) {
        return BindingBuilder
                .bind(inventoryQueue)
                .to(inventoryExchange)
                .with(ORDER_TO_INVENTORY_ROUTING_KEY);
    }

    @Bean
    public Binding notificationBinding(Queue notificationQueue, DirectExchange notificationExchange) {
        return BindingBuilder
                .bind(notificationQueue)
                .to(notificationExchange)
                .with(ORDER_TO_NOTIFICATION_ROUTING_KEY);
    }

    @Bean
    public Binding imageProcessingBinding(Queue imageProcessingQueue, DirectExchange imageProcessingExchange) {
        return BindingBuilder
                .bind(imageProcessingQueue)
                .to(imageProcessingExchange)
                .with(IMAGE_PROCESSING_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
} 