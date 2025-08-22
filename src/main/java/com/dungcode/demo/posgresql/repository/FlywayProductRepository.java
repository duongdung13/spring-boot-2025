package com.dungcode.demo.posgresql.repository;

import com.dungcode.demo.posgresql.entity.FlywayProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlywayProductRepository extends JpaRepository<FlywayProduct, Long> {
}