package com.dungcode.demo.controller;

import com.dungcode.demo.service.FlywayProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/flyway-products")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Flyway Products Controller", description = "Manage flyway products and comments")
public class FlywayProductController {

    private final FlywayProductService flywayProductService;

    @GetMapping("/{productId}/comments")
    public ResponseEntity<?> getProductComments(
            @PathVariable Long productId) {
        return flywayProductService.getProductComments(productId).responseEntity();
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductWithComments(@PathVariable Long productId) {
        return flywayProductService.getProductWithComments(productId).responseEntity();
    }
}
