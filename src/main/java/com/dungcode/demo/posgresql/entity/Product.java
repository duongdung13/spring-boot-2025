package com.dungcode.demo.posgresql.entity;

import jakarta.persistence.Entity;
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
}
