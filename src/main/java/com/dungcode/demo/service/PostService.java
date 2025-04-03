package com.dungcode.demo.service;

import com.dungcode.demo.common.ApiResponse;
import com.dungcode.demo.common.SuccessResponse;
import com.dungcode.demo.dto.request.PostRequest;
import com.dungcode.demo.mongodb.model.Post;
import com.dungcode.demo.mongodb.repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public ApiResponse<?> create(PostRequest dto) {
        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());

        return new SuccessResponse<>(postRepository.save(post));
    }


}
