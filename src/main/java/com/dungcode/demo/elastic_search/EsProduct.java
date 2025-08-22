package com.dungcode.demo.elastic_search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EsProduct {
    private String id;
    private String name;
    private String description;
    private Double price;
    private List<String> tags;
}
