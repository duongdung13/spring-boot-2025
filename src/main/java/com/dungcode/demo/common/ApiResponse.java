package com.dungcode.demo.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class ApiResponse<T> {
    private final Integer statusCode;
    private final String message;
    private final HttpStatus httpStatus;
    private final T data;

    protected ApiResponse(Integer statusCode, String message, T data, HttpStatus httpStatus) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
        this.httpStatus = httpStatus;
    }

    public Map<String, Object> toBody() {
        Map<String, Object> body = new HashMap<>();
        body.put("data", this.data == null ? new HashMap<>() : this.data);
        body.put("message", this.message);
        body.put("status", this.statusCode);

        return body;
    }

    public ResponseEntity<Map<String, Object>> responseEntity() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.toBody());
    }
}