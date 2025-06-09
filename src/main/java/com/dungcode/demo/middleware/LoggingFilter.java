package com.dungcode.demo.middleware;

import com.dungcode.demo.mongodb.model.RequestLog;
import com.dungcode.demo.mongodb.repository.RequestLogRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class LoggingFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    private final RequestLogRepository requestLogRepository;

    public LoggingFilter(RequestLogRepository requestLogRepository) {
        this.requestLogRepository = requestLogRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        long startTime = System.currentTimeMillis();
        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            logRequest(wrappedRequest);
            logResponse(wrappedResponse);

            long duration = System.currentTimeMillis() - startTime;
            this.saveLog(wrappedRequest, wrappedResponse, duration);

            wrappedResponse.copyBodyToResponse();
        }
    }

    private void logRequest(ContentCachingRequestWrapper request) {
        String requestBody = new String(request.getContentAsByteArray(), StandardCharsets.UTF_8);
        logger.info("REQUEST [{} {}] Body: {}", request.getMethod(), request.getRequestURI(), requestBody);
    }

    private void logResponse(ContentCachingResponseWrapper response) {
        String responseBody = new String(response.getContentAsByteArray(), StandardCharsets.UTF_8);
        logger.info("RESPONSE Status: {} Body: {}", response.getStatus(), responseBody);
    }

    private void saveLog(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response, long duration) {
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
            String requestBody = new String(request.getContentAsByteArray(), StandardCharsets.UTF_8);
            log.setRequestBody(requestBody);
            log.setResponseStatus(response.getStatus());
            String responseBody = new String(response.getContentAsByteArray(), StandardCharsets.UTF_8);
            log.setResponseBody(responseBody);
            log.setCreatedAt(LocalDateTime.now());

            this.requestLogRepository.save(log);

            logger.info("API Log - {} {} - Status: {} - Duration: {}ms",
                    request.getMethod(), request.getRequestURI(), response.getStatus(), duration);
        } catch (Exception e) {
            logger.error("Failed to save request log", e);
        }
    }
}
