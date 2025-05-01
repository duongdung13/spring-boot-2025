package com.dungcode.demo.service;

import com.dungcode.demo.common.ApiResponse;
import com.dungcode.demo.common.SuccessResponse;
import com.dungcode.demo.posgresql.entity.Product;
import com.dungcode.demo.posgresql.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImplement implements ProductService {
    private final ProductRepository productRepository;

    @Override
    @Cacheable(value = "products", key = "#id")
    public Product getProductById(Long id) {
        log.info("Fetching product from database for id: {}", id);
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    @Override
    @CachePut(value = "products", key = "#product.id")
    public Product updateProduct(Long id, Product product) {
        log.info("Starting update product with id: {}", id);
        return productRepository.save(product);
    }

    @CacheEvict(value = "products", allEntries = true)
    public ApiResponse<?> clearProductCache() {
        log.info("Clearing entire product cache");
        return new SuccessResponse<>(true);
    }
}
