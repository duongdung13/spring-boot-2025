package com.dungcode.demo.posgresql.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "orders")
public class OrderEvent extends AbstractEntity implements Serializable {
    private String orderId;
    private String eventType; // CREATED, UPDATED, CANCELLED
    private String customerId;
    private Date timestamp;
    private OrderDetails orderDetails;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderDetails implements Serializable {
        private String productId;
        private Integer quantity;
        private Double totalAmount;
    }
} 