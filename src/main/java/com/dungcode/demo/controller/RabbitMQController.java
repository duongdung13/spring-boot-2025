package com.dungcode.demo.controller;

import com.dungcode.demo.common.SuccessResponse;
import com.dungcode.demo.posgresql.entity.OrderEvent;
import com.dungcode.demo.service.RabbitMQProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class RabbitMQController {

    private final RabbitMQProducer rabbitMQProducer;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderEvent.OrderDetails orderDetails) {
        // Create order event
        OrderEvent orderEvent = new OrderEvent(
                UUID.randomUUID().toString(),
                "CREATED",
                "customer123", // In real application, this would come from authentication
                new Date(),
                orderDetails
        );

        // Send order event to RabbitMQ
        rabbitMQProducer.sendOrderEvent(orderEvent);

        return (new SuccessResponse<>("Order created and events published successfully")).responseEntity();
    }
} 