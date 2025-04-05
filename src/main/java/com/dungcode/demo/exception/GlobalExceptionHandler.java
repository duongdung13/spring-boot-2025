package com.dungcode.demo.exception;

import com.dungcode.demo.common.ErrorResponse;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.nio.file.AccessDeniedException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<?> handleAllException(Exception exception, HttpServletRequest request) {
//        ErrorResponse<?> errorResponse = new ErrorResponse<>();
//        ex.printStackTrace();
        //return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse.toBody());
        System.out.println("❗❗❗Exception");

        return this.responseHandleError(exception, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //test ok
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<?> handleNoResourceFoundException(NoResourceFoundException exception, HttpServletRequest request) {
        System.out.println("❗❗❗NoResourceFoundException");

        return this.responseHandleError(exception, request, HttpStatus.NOT_FOUND);
    }

    //test ok
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException exception, HttpServletRequest request) {
        return this.responseHandleError(exception, request, HttpStatus.METHOD_NOT_ALLOWED);
    }

    //test ok
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<?> handleValidationException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        System.out.println("MethodArgumentNotValidException");
        return this.responseHandleError(exception, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<?> handleNotFoundException(NotFoundException exception, HttpServletRequest request) {
        return this.responseHandleError(exception, request, HttpStatus.NOT_FOUND);
    }

    // Custom 1 exception
    public static class NotFoundException extends RuntimeException {
        public NotFoundException(String message) {
            super(message);
        }
    }

    @ExceptionHandler(NullPointerException.class)
    public final ResponseEntity<?> handleNullPointerException(NullPointerException ex) {
        System.out.println("NullPointerException");
        ErrorResponse<?> errorResponse = new ErrorResponse<>(this.getCustomMessage("Null Pointer Exception", ex));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse.toBody());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorResponse<?> errorResponse = new ErrorResponse<>(this.getCustomMessage("Illegal Argument Exception", ex));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse.toBody());
    }


    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingServletRequestParameter(MissingServletRequestParameterException ex) {
        System.out.println("MissingServletRequestParameterException");
        ErrorResponse<?> errorResponse = new ErrorResponse<>(this.getCustomMessage(null, ex));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse.toBody());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex) {
        ErrorResponse<?> errorResponse = new ErrorResponse<>(this.getCustomMessage("Access Denied Exception", ex));
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse.toBody());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxSizeException(MaxUploadSizeExceededException ex) {
        ErrorResponse<?> errorResponse = new ErrorResponse<>(this.getCustomMessage(null, ex));
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(errorResponse.toBody());
    }

    //DataIntegrityViolationException
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolationException(DataIntegrityViolationException ex, HttpServletRequest request) {
        return this.responseHandleError(ex, request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<?> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
        ErrorResponse<?> errorResponse = new ErrorResponse<>(this.getCustomMessage(null, ex));
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(errorResponse.toBody());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<?> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        ErrorResponse<?> errorResponse = new ErrorResponse<>(this.getCustomMessage(null, ex));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse.toBody());
    }

    @ExceptionHandler(ConversionFailedException.class)
    public ResponseEntity<?> handleConversionFailedException(ConversionFailedException exception, HttpServletRequest request) {
        return this.responseHandleError(exception, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex) {
        String message = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));

        ErrorResponse<?> errorResponse = new ErrorResponse<>(message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse.toBody());
    }


    private String getCustomMessage(String messageDefault, Exception ex) {
        return (ex.getMessage() == null || ex.getMessage().trim().isEmpty()) ? messageDefault : ex.getMessage();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception, HttpServletRequest request) {
        String messageDefault = "Incorrect JSON syntax or data type error";
        Throwable cause = exception.getCause();

        if (cause instanceof InvalidFormatException ife) {
            messageDefault = "Invalid value: " + ife.getValue();
        }

        ErrorResponse<?> response = new ErrorResponse<>(messageDefault, 422);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response.toBody());
    }

    private ResponseEntity<?> responseHandleError(Exception exception, HttpServletRequest request, HttpStatus httpStatus) {
        System.out.println("responseHandleError" + httpStatus.value());

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", new Date());
        response.put("status", httpStatus.value());
        response.put("path", request.getRequestURI());
        response.put("error", httpStatus.getReasonPhrase());
        response.put("message", exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
//
//    private String getStringValidation(String acceptLanguageHeader, String key) {
//        try {
//            Locale locale = new Locale(acceptLanguageHeader);
//            ResourceBundle validations = ResourceBundle.getBundle("i18n.messages", locale);
//            return validations.getString(key);
//        } catch (Exception e) {
//            return null;
//        }
//    }
}
