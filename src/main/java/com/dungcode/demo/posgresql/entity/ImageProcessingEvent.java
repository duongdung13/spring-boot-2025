package com.dungcode.demo.posgresql.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageProcessingEvent implements Serializable {
    private String imageId;
    private String originalImageUrl;
    private String processingType; // RESIZE, COMPRESS, WATERMARK, etc.
    private ProcessingStatus status;
    private Date timestamp;
    private String workerId; // ID of the worker processing this image
    private ProcessingOptions options;

    public enum ProcessingStatus {
        PENDING,
        PROCESSING,
        COMPLETED,
        FAILED
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProcessingOptions implements Serializable {
        private Integer targetWidth;
        private Integer targetHeight;
        private Integer quality; // For compression
        private String watermarkText;
        private String outputFormat; // JPG, PNG, etc.
    }
} 