/**
 * Copyright (c) 2025 dungduong
 * Sản phẩm: LocaAI Glasses
 *
 * @author dungduong
 * @version 1.0
 * @since 21/07/2025
 */


package com.dungcode.demo.controller;

import com.dungcode.demo.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transfers")
@Slf4j
@Tag(name = "Transfers Controller", description = "Quản lý chuyển tiền")
public class TransferController {
    private final UserService userService;

    public TransferController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> transfer(
            @RequestParam Long fromUserId,
            @RequestParam Long toUserId,
            @RequestParam Long amount
    ) {
        try {
            return userService.transfer(fromUserId, toUserId, amount).responseEntity();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fail: " + e.getMessage());
        }
    }
}
