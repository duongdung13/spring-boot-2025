package com.dungcode.demo.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class SuccessResponse<T> extends ApiResponse<T> {
    private static final Integer STATUS_CODE = 200;

    public SuccessResponse() {
        super(200, "Success", null, HttpStatus.OK);
    }

    public SuccessResponse(T data) {
        super(STATUS_CODE, "Success", data, HttpStatus.OK);
    }

    public SuccessResponse(T data, String message) {
        super(STATUS_CODE, message, data, HttpStatus.OK);
    }

    public SuccessResponse(T data, String message, Integer statusCode) {
        super(statusCode, message, data, HttpStatus.OK);
    }

    public SuccessResponse(String message) {
        super(STATUS_CODE, message, null, HttpStatus.OK);
    }
}
