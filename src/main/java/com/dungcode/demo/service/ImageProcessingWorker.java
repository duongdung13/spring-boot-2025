package com.dungcode.demo.service;

import com.dungcode.demo.config.RabbitMQConfig;
import com.dungcode.demo.posgresql.entity.ImageProcessingEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class ImageProcessingWorker {

    private final String workerId = UUID.randomUUID().toString();

    @RabbitListener(queues = RabbitMQConfig.IMAGE_PROCESSING_QUEUE)
    public void processImage(ImageProcessingEvent event) {
        try {
            log.info("Worker {} starting to process image: {}", workerId, event.getImageId());

            // Set worker ID and update status
            event.setWorkerId(workerId);
            event.setStatus(ImageProcessingEvent.ProcessingStatus.PROCESSING);

            // Simulate image processing based on type
            switch (event.getProcessingType().toUpperCase()) {
                case "RESIZE":
                    processResize(event);
                    break;
                case "COMPRESS":
                    processCompress(event);
                    break;
                case "WATERMARK":
                    processWatermark(event);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported processing type: " + event.getProcessingType());
            }

            // Update status to completed
            event.setStatus(ImageProcessingEvent.ProcessingStatus.COMPLETED);
            log.info("Worker {} completed processing image: {}", workerId, event.getImageId());

        } catch (Exception e) {
            log.error("Worker {} failed to process image: {}", workerId, event.getImageId(), e);
            event.setStatus(ImageProcessingEvent.ProcessingStatus.FAILED);
            // In a real application, you might want to retry or move to a dead letter queue
        }
    }

    private void processResize(ImageProcessingEvent event) throws InterruptedException {
        // Simulate resize processing
        log.info("Resizing image to {}x{}",
                event.getOptions().getTargetWidth(),
                event.getOptions().getTargetHeight());
        TimeUnit.SECONDS.sleep(2); // Simulate processing time
    }

    private void processCompress(ImageProcessingEvent event) throws InterruptedException {
        // Simulate compression processing
        log.info("Compressing image with quality: {}",
                event.getOptions().getQuality());
        TimeUnit.SECONDS.sleep(1); // Simulate processing time
    }

    private void processWatermark(ImageProcessingEvent event) throws InterruptedException {
        // Simulate watermark processing
        log.info("Adding watermark: {}",
                event.getOptions().getWatermarkText());
        TimeUnit.SECONDS.sleep(3); // Simulate processing time
    }
} 