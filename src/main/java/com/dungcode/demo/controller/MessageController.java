/**
 * Copyright (c) 2025 dungduong
 * Sản phẩm: LocaAI Glasses
 *
 * @author dungduong
 * @version 1.0
 * @since 21/07/2025
 */


package com.dungcode.demo.controller;

import com.dungcode.demo.common.SuccessResponse;
import com.dungcode.demo.redis.MessagePublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/publish")
public class MessageController {
    private final MessagePublisher publisher;

    public MessageController(MessagePublisher publisher) {
        this.publisher = publisher;
    }

    @PostMapping
    public ResponseEntity<?> publishMessage(@RequestParam String message) {
        publisher.publish(message);
        return new SuccessResponse<>("Success").responseEntity();
    }
}
