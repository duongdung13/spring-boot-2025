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
@Entity(name = "inventories")
public class InventoryEvent extends AbstractEntity implements Serializable {
    private String inventoryId;
    private String productId;
    private Integer quantity;
    private String operation; // STOCK_UPDATE, RESERVATION, RELEASE
    private Date timestamp;
    private String orderId; // Reference to the order that triggered this event
} 