package com.dungcode.demo.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ErrorResponse<T> extends ApiResponse<T> {
    private static final Integer STATUS_CODE = 400;

    public ErrorResponse() {
        super(500, "The system is under maintenance, please try again later.", null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ErrorResponse(String message, Integer statusCode, T data) {
        super(statusCode, message, data, HttpStatus.BAD_REQUEST);
    }

    public ErrorResponse(String message) {
        super(STATUS_CODE, message, null, HttpStatus.BAD_REQUEST);
    }

    public ErrorResponse(Integer statusCode, String message) {
        super(statusCode, message, null, HttpStatus.BAD_REQUEST);
    }

    public ErrorResponse(String message, Integer statusCode) {
        super(statusCode, message, null, HttpStatus.BAD_REQUEST);
    }
}
