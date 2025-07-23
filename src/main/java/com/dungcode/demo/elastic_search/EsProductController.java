package com.dungcode.demo.elastic_search;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/elastic-search/products")
@RequiredArgsConstructor
public class EsProductController {

    private final EsProductService productService;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody EsProduct product) throws IOException {
        return (productService.save(product)).responseEntity();
    }

    @GetMapping("/search")
    public ResponseEntity<List<EsProduct>> search(@RequestParam String q) throws IOException {
        return ResponseEntity.ok(productService.searchAllFields(q));
    }
}