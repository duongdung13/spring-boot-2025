package com.dungcode.demo.posgresql.repository;

import com.dungcode.demo.posgresql.entity.FlywayProduct;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FlywayProductRepository extends JpaRepository<FlywayProduct, Long> {
    @Query("SELECT p FROM FlywayProduct p LEFT JOIN FETCH p.comments WHERE p.id = :id")
    Optional<FlywayProduct> findByIdWithComments(@Param("id") Long id);

}