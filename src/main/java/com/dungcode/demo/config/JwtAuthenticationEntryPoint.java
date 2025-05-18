package com.dungcode.demo.config;

import com.dungcode.demo.mongodb.model.RequestLog;
import com.dungcode.demo.mongodb.repository.RequestLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

    private final RequestLogRepository requestLogRepository;

    public JwtAuthenticationEntryPoint(RequestLogRepository requestLogRepository) {
        this.requestLogRepository = requestLogRepository;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        logger.error("Authentication failed: {}", authException.getMessage(), authException);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", new Date().toInstant().toString());
        errorResponse.put("status", HttpStatus.UNAUTHORIZED.value());
        errorResponse.put("path", request.getRequestURI());
        errorResponse.put("error", authException.getMessage());
        errorResponse.put("message", HttpStatus.UNAUTHORIZED.getReasonPhrase());

        this.saveLogError(request, response, authException);

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        response.flushBuffer();
    }

    private void logRequest(ContentCachingRequestWrapper request) {
        String requestBody = new String(request.getContentAsByteArray(), StandardCharsets.UTF_8);
        logger.info("REQUEST [{} {}] Body: {}", request.getMethod(), request.getRequestURI(), requestBody);
    }

    private void logResponse(ContentCachingResponseWrapper response) {
        String responseBody = new String(response.getContentAsByteArray(), StandardCharsets.UTF_8);
        logger.info("RESPONSE Status: {} Body: {}", response.getStatus(), responseBody);
    }

    private void saveLogError(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        logRequest(wrappedRequest);
        logResponse(wrappedResponse);

        this.saveLog(wrappedRequest, wrappedResponse, authException);
    }

    private void saveLog(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response, AuthenticationException authException) {
        try {
            RequestLog log = new RequestLog();
            log.setMethod(request.getMethod());
            log.setUri(request.getRequestURI());
            log.setClientIp(request.getRemoteAddr());
            Map<String, String> headers = new HashMap<>();
            Collections.list(request.getHeaderNames()).forEach(headerName ->
                    headers.put(headerName, request.getHeader(headerName))
            );
            log.setHeaders(headers);
            log.setResponseStatus(response.getStatus());
            log.setError(authException.getMessage());
            log.setCreatedAt(LocalDateTime.now());

            this.requestLogRepository.save(log);

            logger.info("API Log - {} {} - Status: {}",
                    request.getMethod(), request.getRequestURI(), response.getStatus());
        } catch (Exception e) {
            logger.error("Failed to save request log", e);
        }
    }

}
