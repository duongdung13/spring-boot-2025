package com.dungcode.demo.exception;

import lombok.Setter;

import java.util.Date;

@Setter
public class ExceptionErrorResponse {
    private Date timestamp;
    private int status;
    private String path;
    private String error;
    private String message;
}
