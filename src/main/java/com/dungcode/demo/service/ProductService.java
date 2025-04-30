package com.dungcode.demo.service;

import com.dungcode.demo.posgresql.entity.Product;
import com.dungcode.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * This method demonstrates the @Cacheable annotation.
     * The result of this method will be cached with a key based on the id parameter.
     * If the same id is requested again, the method will not be executed,
     * and the cached result will be returned instead.
     */
    @Cacheable(value = "products", key = "#id")
    public Product getProductById(Long id) {
        System.out.println("Fetching product from database for id: " + id);
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    /**
     * This method demonstrates the @CachePut annotation.
     * It always executes the method and updates the cache with the result.
     * Useful for update operations where you want to update the cache as well.
     */
    @CachePut(value = "products", key = "#product.id")
    public Product updateProduct(Product product) {
        System.out.println("Updating product in database: " + product);
        // In a real application, you would update the product in the database here
        return product;
    }

    /**
     * This method demonstrates the @CacheEvict annotation.
     * It removes an entry from the cache.
     * Useful when you delete an entity and want to remove it from the cache as well.
     */
    @CacheEvict(value = "products", key = "#id")
    public void deleteProduct(Long id) {
        System.out.println("Deleting product from cache with id: " + id);
        // In a real application, you would delete the product from the database here
    }

    /**
     * This method demonstrates how to clear the entire cache.
     */
    @CacheEvict(value = "products", allEntries = true)
    public void clearProductCache() {
        System.out.println("Clearing entire product cache");
    }
}
