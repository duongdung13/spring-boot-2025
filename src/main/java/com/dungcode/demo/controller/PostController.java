package com.dungcode.demo.controller;

import com.dungcode.demo.dto.request.PostRequest;
import com.dungcode.demo.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
public class PostController {
    PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping()
    ResponseEntity<?> create(@RequestBody @Valid PostRequest request) {
        return (postService.create(request)).responseEntity();
    }
}
