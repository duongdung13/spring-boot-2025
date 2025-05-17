package com.dungcode.demo.mongodb.model;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@Document(collection = "request_logs")
public class RequestLog {
    @Id
    private String id;
    private String method;
    private String uri;
    private String clientIp;
    private Map<String, String> headers;
    private String requestBody;
    private Integer responseStatus;
    private String responseBody;
    private LocalDateTime createdAt;
}
