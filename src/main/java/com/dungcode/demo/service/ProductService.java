package com.dungcode.demo.service;

import com.dungcode.demo.common.ApiResponse;
import com.dungcode.demo.posgresql.entity.Product;

public interface ProductService {
    public Product getProductById(Long id);

    public Product updateProduct(Long id, Product request);

    public ApiResponse<?> clearProductCache();
}
