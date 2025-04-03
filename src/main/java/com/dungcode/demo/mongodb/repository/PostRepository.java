package com.dungcode.demo.mongodb.repository;

import com.dungcode.demo.mongodb.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {

    Optional<Post> findFirstById(String id);
}
