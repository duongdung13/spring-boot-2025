package com.dungcode.demo.posgresql.repository;

import com.dungcode.demo.posgresql.entity.FlywayProductComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlywayProductCommentRepository extends JpaRepository<FlywayProductComment, Long> {
    List<FlywayProductComment> findByProductIdOrderByCreatedAtDesc(Long productId);
}