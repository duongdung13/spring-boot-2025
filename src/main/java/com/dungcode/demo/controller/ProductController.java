package com.dungcode.demo.controller;

import com.dungcode.demo.common.SuccessResponse;
import com.dungcode.demo.posgresql.entity.Product;
import com.dungcode.demo.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@Slf4j
@Tag(name = "Products Controller", description = "Quản lý danh sách sản phẩm")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Long id) {
        return (new SuccessResponse<>((productService.getProductById(id)))).responseEntity();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @Valid @RequestBody Product product) {
        product.setId(id);
        return (new SuccessResponse<>((productService.updateProduct(id, product)))).responseEntity();
    }

    @DeleteMapping("/cache/clear")
    public ResponseEntity<?> clearCache() {
        return (productService.clearProductCache()).responseEntity();
    }

    @GetMapping("/{id}/set-cache")
    public ResponseEntity<?> setCache(@PathVariable Long id) {
        return (productService.setCache(id)).responseEntity();
    }

    @GetMapping("/{id}/get-cache")
    public ResponseEntity<?> getCache(@PathVariable Long id) {
        return (productService.getCache(id)).responseEntity();
    }

}
