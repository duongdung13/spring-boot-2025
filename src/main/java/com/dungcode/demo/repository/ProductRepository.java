package com.dungcode.demo.repository;

import com.dungcode.demo.posgresql.entity.Product;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class ProductRepository {

    // Simulating a database with a HashMap
    private final Map<Long, Product> productDb = new HashMap<>();

    // Initialize with some sample data
    public ProductRepository() {
//        productDb.put(1L, new Product(1L, "Laptop", "High-performance laptop", 1200.0));
//        productDb.put(2L, new Product(2L, "Smartphone", "Latest smartphone model", 800.0));
//        productDb.put(3L, new Product(3L, "Tablet", "Lightweight tablet", 500.0));
    }

    public Optional<Product> findById(Long id) {
        // Simulate a slow database operation
        simulateSlowService();
        return Optional.ofNullable(productDb.get(id));
    }

    // Method to simulate a slow service
    private void simulateSlowService() {
        try {
            // Simulate a 2-second delay
            Thread.sleep(2000);
            System.out.println("Database operation completed after delay");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
