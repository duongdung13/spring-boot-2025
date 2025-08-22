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
@Entity(name = "product_comments")
public class ProductComment extends AbstractEntity {
    private Long productId;
    private String comment;
    private String authorName;
}
