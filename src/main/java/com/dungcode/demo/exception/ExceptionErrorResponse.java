package com.dungcode.demo.exception;

import lombok.Data;

import java.util.Date;

@Data
public class ExceptionErrorResponse {
    private Date timestamp;
    private int status;
    private String path;
    private String error;
    private String message;
}
