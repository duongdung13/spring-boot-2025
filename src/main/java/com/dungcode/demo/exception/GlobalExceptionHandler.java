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
import org.springframework.validation.FieldError;
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
        System.out.println("❗❗❗Exception❗❗❗");

        return this.responseHandleError(exception, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<?> handleNoResourceFoundException(NoResourceFoundException exception, HttpServletRequest request) {
        System.out.println("❗❗❗NoResourceFoundException❗❗❗");

        return this.responseHandleError(exception, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException exception, HttpServletRequest request) {
        System.out.println("❗❗❗HttpRequestMethodNotSupportedException❗❗❗");
        return this.responseHandleError(exception, request, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<?> handleValidationException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        System.out.println("❗❗❗MethodArgumentNotValidException❗❗❗");
        return this.responseHandleError(exception, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<?> handleNotFoundException(NotFoundException exception, HttpServletRequest request) {
        System.out.println("❗❗❗NotFoundException❗❗❗");
        return this.responseHandleError(exception, request, HttpStatus.NOT_FOUND);
    }

    // Custom 1 exception
    public static class NotFoundException extends RuntimeException {
        public NotFoundException(String message) {
            super(message);
        }
    }

    @ExceptionHandler(NullPointerException.class)
    public final ResponseEntity<?> handleNullPointerException(NullPointerException exception, HttpServletRequest request) {
        System.out.println("❗❗❗NullPointerException❗❗❗");
        return this.responseHandleError(exception, request, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(IndexOutOfBoundsException.class)
    public final ResponseEntity<?> handleIndexOutOfBoundsException(IndexOutOfBoundsException exception, HttpServletRequest request) {
        System.out.println("❗❗❗IndexOutOfBoundsException❗❗❗");
        return this.responseHandleError(exception, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException exception, HttpServletRequest request) {
        System.out.println("❗❗❗IllegalArgumentException❗❗❗");
        return this.responseHandleError(exception, request, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingServletRequestParameter(MissingServletRequestParameterException exception, HttpServletRequest request) {
        System.out.println("❗❗❗MissingServletRequestParameterException❗❗❗");
        return this.responseHandleError(exception, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxSizeException(MaxUploadSizeExceededException exception, HttpServletRequest request) {
        System.out.println("❗❗❗MaxUploadSizeExceededException❗❗❗");
        return this.responseHandleError(exception, request, HttpStatus.PAYLOAD_TOO_LARGE);
    }

    //DataIntegrityViolationException
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolationException(DataIntegrityViolationException exception, HttpServletRequest request) {
        System.out.println("❗❗❗DataIntegrityViolationException❗❗❗");
        return this.responseHandleError(exception, request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<?> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException exception, HttpServletRequest request) {
        System.out.println("❗❗❗HttpMediaTypeNotSupportedException❗❗❗");
        return this.responseHandleError(exception, request, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<?> handleNoHandlerFoundException(NoHandlerFoundException exception, HttpServletRequest request) {
        System.out.println("❗❗❗HttpMediaTypeNotSupportedException❗❗❗");
        return this.responseHandleError(exception, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConversionFailedException.class)
    public ResponseEntity<?> handleConversionFailedException(ConversionFailedException exception, HttpServletRequest request) {
        System.out.println("❗❗❗ConversionFailedException❗❗❗");
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
        System.out.println("❗❗❗HttpMessageNotReadableException❗❗❗");
        return this.responseHandleError(exception, request, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    private ResponseEntity<?> responseHandleError(Exception exception, HttpServletRequest request, HttpStatus httpStatus) {
        System.out.println("responseHandleError : " + httpStatus.value());

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", new Date());
        response.put("status", httpStatus.value());
        response.put("path", request.getRequestURI());
        response.put("error", httpStatus.getReasonPhrase());
        response.put("message", exception.getMessage());

        if (exception instanceof MethodArgumentNotValidException manve) {
            List<FieldError> fieldErrors = manve.getBindingResult().getFieldErrors();
            if (!fieldErrors.isEmpty()) {
                response.put("message", manve.getAllErrors().get(0).getDefaultMessage());
            } else {
                response.put("message", manve.getAllErrors().get(0).getDefaultMessage());
            }
        }

        if (exception instanceof HttpMessageNotReadableException) {
            Throwable cause = exception.getCause();
            if (cause instanceof InvalidFormatException ife) {
                response.put("message", "Invalid value: " + ife.getValue());
            } else {
                response.put("message", "Invalid value");
            }
        }

        return ResponseEntity.status(httpStatus.value())
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    private String getStringValidation(String acceptLanguageHeader, String key) {
        try {
            Locale locale = new Locale(acceptLanguageHeader);
            ResourceBundle validations = ResourceBundle.getBundle("i18n.messages", locale);
            return validations.getString(key);
        } catch (Exception e) {
            return null;
        }
    }

}
