package com.dungcode.demo.service;

import com.dungcode.demo.config.RabbitMQConfig;
import com.dungcode.demo.posgresql.entity.ImageProcessingEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageProcessingProducer {

    private final RabbitTemplate rabbitTemplate;

    public void submitImageForProcessing(String imageUrl, String processingType, ImageProcessingEvent.ProcessingOptions options) {
        ImageProcessingEvent event = new ImageProcessingEvent(
                UUID.randomUUID().toString(),
                imageUrl,
                processingType,
                ImageProcessingEvent.ProcessingStatus.PENDING,
                new Date(),
                null, // workerId will be set by the worker
                options
        );

        log.info("Submitting image for processing: {}", event);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.IMAGE_PROCESSING_EXCHANGE,
                RabbitMQConfig.IMAGE_PROCESSING_ROUTING_KEY,
                event
        );
    }
} 