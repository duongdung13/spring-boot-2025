package com.dungcode.demo.service;

import com.dungcode.demo.common.ApiResponse;
import com.dungcode.demo.common.SuccessResponse;
import com.dungcode.demo.dto.request.PostRequest;
import com.dungcode.demo.exception.GlobalExceptionHandler;
import com.dungcode.demo.mongodb.model.Post;
import com.dungcode.demo.mongodb.repository.PostRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public ApiResponse<?> create(PostRequest dto) {
        if (postRepository.existsPostByTitle(dto.getTitle().trim())) {
            throw new DataIntegrityViolationException("Exist post title");
        }

        Post post = new Post();
        post.setTitle(dto.getTitle().trim());
        post.setContent(dto.getContent());

        return new SuccessResponse<>(postRepository.save(post));
    }

    public Post getPostById(String id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new GlobalExceptionHandler.NotFoundException("Post not found"));
    }
}
