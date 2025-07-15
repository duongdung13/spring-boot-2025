package com.dungcode.demo.service;

import com.dungcode.demo.common.ApiResponse;
import com.dungcode.demo.common.SuccessResponse;
import com.dungcode.demo.dto.request.ProductCreateRequest;
import com.dungcode.demo.posgresql.entity.Product;
import com.dungcode.demo.posgresql.entity.ProductComment;
import com.dungcode.demo.posgresql.repository.ProductCommentRepository;
import com.dungcode.demo.posgresql.repository.ProductRepository;
import jakarta.transaction.Transactional;
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
    private final ProductCommentRepository productCommentRepository;
    private final CacheService cacheService;

    @Override
    @Transactional
    public ApiResponse<?> createProduct(ProductCreateRequest request) {
        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .build();
        Product savedProduct = productRepository.save(product);

        if( request.getName().equals("Iphone 11")) {
            throw new RuntimeException("Iphone 11 is not allowed to be created");
        }

        ProductComment comment = ProductComment.builder()
                .comment("Initial comment for product " + savedProduct.getName())
                .authorName("System")
                .productId(savedProduct.getId())
                .build();
        ProductComment savedProductComment = productCommentRepository.save(comment);

        return new SuccessResponse<>(savedProduct);
    }

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

    @Override
    public ApiResponse<?> setCache(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        this.cacheService.setCache("products::" + id, product, 60);
        log.info("Setting cache for product with id: {}", id);
        return new SuccessResponse<>(true);
    }

    @Override
    public ApiResponse<?> getCache(Long id) {
        Product product = (Product) this.cacheService.getCache("products::" + id);

        if (product == null) {
            log.info("Cache miss for product with id: {}", id);
            return new SuccessResponse<>(false, "Cache miss");
        }

        return new SuccessResponse<>(product);
    }
}
