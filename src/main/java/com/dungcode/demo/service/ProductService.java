package com.dungcode.demo.service;

import com.dungcode.demo.common.ApiResponse;
import com.dungcode.demo.dto.request.ProductCreateRequest;
import com.dungcode.demo.posgresql.entity.Product;

public interface ProductService {
    public ApiResponse<?> createProduct(ProductCreateRequest request);

    public Product getProductById(Long id);

    public Product updateProduct(Long id, Product request);

    public ApiResponse<?> clearProductCache();

    public ApiResponse<?> setCache(Long id);

    public ApiResponse<?> getCache(Long id);

    public ApiResponse<?> purchaseProduct(Long productId);
}
