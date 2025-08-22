package com.dungcode.demo.service;

import com.dungcode.demo.common.ApiResponse;
import com.dungcode.demo.common.SuccessResponse;
import com.dungcode.demo.posgresql.entity.FlywayProduct;
import com.dungcode.demo.posgresql.entity.FlywayProductComment;
import com.dungcode.demo.posgresql.repository.FlywayProductCommentRepository;
import com.dungcode.demo.posgresql.repository.FlywayProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FlywayProductService {
    private final FlywayProductRepository flywayProductRepository;
    private final FlywayProductCommentRepository flywayProductCommentRepository;

    public ApiResponse<?> getProductComments(Long productId) {
        // Verify product exists
        if (!flywayProductRepository.existsById(productId)) {
            throw new RuntimeException("Product not found with id: " + productId);
        }

        List<FlywayProductComment> comments = flywayProductCommentRepository
                .findByProductIdOrderByCreatedAtDesc(productId);

        return new SuccessResponse<>(comments);
    }

    public ApiResponse<FlywayProduct> getProductWithComments(Long productId) {
//        FlywayProduct product = flywayProductRepository.findById(productId)
//                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        FlywayProduct product = flywayProductRepository.findByIdWithComments(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        return new SuccessResponse<>(product);
    }
}
