package com.dungcode.demo.controller;

import com.dungcode.demo.common.SuccessResponse;
import com.dungcode.demo.posgresql.entity.ImageProcessingEvent;
import com.dungcode.demo.service.ImageProcessingProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageProcessingController {

    private final ImageProcessingProducer imageProcessingProducer;

    @PostMapping("/process")
    public ResponseEntity<?> processImage(
            @RequestParam String imageUrl,
            @RequestParam String processingType,
            @RequestBody ImageProcessingEvent.ProcessingOptions options) {

        imageProcessingProducer.submitImageForProcessing(imageUrl, processingType, options);
        return (new SuccessResponse<>("Image submitted for processing successfully")).responseEntity();
    }

    @PostMapping("/process/resize")
    public ResponseEntity<?> resizeImage(
            @RequestParam String imageUrl,
            @RequestParam Integer width,
            @RequestParam Integer height) {

        ImageProcessingEvent.ProcessingOptions options = new ImageProcessingEvent.ProcessingOptions(
                width,
                height,
                null,
                null,
                "JPG"
        );

        imageProcessingProducer.submitImageForProcessing(imageUrl, "RESIZE", options);
        return (new SuccessResponse<>("Image submitted for resizing successfully")).responseEntity();
    }

    @PostMapping("/process/compress")
    public ResponseEntity<?> compressImage(
            @RequestParam String imageUrl,
            @RequestParam Integer quality) {

        ImageProcessingEvent.ProcessingOptions options = new ImageProcessingEvent.ProcessingOptions(
                null,
                null,
                quality,
                null,
                "JPG"
        );

        imageProcessingProducer.submitImageForProcessing(imageUrl, "COMPRESS", options);
        return (new SuccessResponse<>("Image submitted for compression successfully")).responseEntity();
    }

    @PostMapping("/process/watermark")
    public ResponseEntity<?> watermarkImage(
            @RequestParam String imageUrl,
            @RequestParam String watermarkText) {

        ImageProcessingEvent.ProcessingOptions options = new ImageProcessingEvent.ProcessingOptions(
                null,
                null,
                null,
                watermarkText,
                "JPG"
        );

        imageProcessingProducer.submitImageForProcessing(imageUrl, "WATERMARK", options);
        return (new SuccessResponse<>("Image submitted for watermarking successfully")).responseEntity();
    }
} 