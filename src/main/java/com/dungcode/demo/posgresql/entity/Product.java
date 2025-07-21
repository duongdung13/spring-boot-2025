package com.dungcode.demo.posgresql.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Version;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "products")
public class Product extends AbstractEntity {
    private String name;
    private String description;
    private double price;
    private int stock;

    @Version
    @Column(nullable = false)
    private int version = 0;
}
