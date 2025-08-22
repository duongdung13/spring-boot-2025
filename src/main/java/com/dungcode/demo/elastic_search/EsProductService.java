package com.dungcode.demo.elastic_search;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import com.dungcode.demo.common.ApiResponse;
import com.dungcode.demo.common.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EsProductService {
    private final ElasticsearchClient client;
    private final String indexName = "products";

    public ApiResponse<?> save(EsProduct product) throws IOException {
        client.index(i -> i
                .index(indexName)
                .id(product.getId())
                .document(product)
        );

        return new SuccessResponse<>("Success");
    }

    // a. Tìm kiếm full-text + fuzzy
    public List<EsProduct> searchByName(String keyword) throws IOException {
        SearchResponse<EsProduct> response = client.search(s -> s
                .index("products")
                .query(q -> q
                        .match(m -> m
                                .field("name")
                                .query(keyword)
                                .fuzziness("AUTO") // cho phép sai chính tả
                        )
                ), EsProduct.class);

        return response.hits().hits().stream()
                .map(Hit::source)
                .collect(Collectors.toList());
    }

    // b. Multi-field + partial match (multi_match)
    public List<EsProduct> searchAllFields(String keyword) throws IOException {
        SearchResponse<EsProduct> response = client.search(s -> s
                .index("products")
                .query(q -> q
                        .multiMatch(m -> m
                                .fields("name", "description", "tags")
                                .query(keyword)
                                .type(TextQueryType.BoolPrefix) // partial match
                        )
                ), EsProduct.class);

        return response.hits().hits().stream()
                .map(Hit::source)
                .collect(Collectors.toList());
    }

    // c. Kết hợp điều kiện (bool query)
    public List<EsProduct> searchAdvanced(String keyword, double minPrice) throws IOException {
        SearchResponse<EsProduct> response = client.search(s -> s
                .index("products")
                .query(q -> q
                        .bool(b -> b
                                .must(m -> m.match(t -> t.field("name").query(keyword)))
                                .filter(f -> f.range(r -> r.field("price").gte(JsonData.of(minPrice))))
                        )
                ), EsProduct.class);

        return response.hits().hits().stream()
                .map(Hit::source)
                .collect(Collectors.toList());
    }

}
