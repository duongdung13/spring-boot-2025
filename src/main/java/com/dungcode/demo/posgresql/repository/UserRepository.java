package com.dungcode.demo.posgresql.repository;

import com.dungcode.demo.enums.Role;
import com.dungcode.demo.posgresql.entity.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Sử dụng JPQL
    @Query("SELECT u FROM users u WHERE u.username = :username")
    User customQueryFindByUsername(@Param("username") String username);

    // Sử dụng native SQL (PostgreSQL)
    @Query(value = "SELECT * FROM users WHERE roles @> CAST(ARRAY[:role] AS varchar[])  ORDER BY id DESC LIMIT 3", nativeQuery = true)
    List<User> customQueryFindActiveUsersByRole(@Param("role") String role);

    // Query với JOIN
//    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName")
//    List<User> findByRoleName(@Param("roleName") String roleName);

    // Query với điều kiện LIKE và PostgreSQL specific
//    @Query(value = "SELECT * FROM users WHERE name ILIKE %:keyword% OR email ILIKE %:keyword%", nativeQuery = true)
//    List<User> searchUsers(@Param("keyword") String keyword);

    List<User> findAllByUsername(@Param("username") String username);

    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    // Sử dụng SELECT FOR UPDATE để lock row
    @Query("SELECT a FROM users a WHERE a.id = :id")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<User> findByAccountNumberForUpdate(@Param("id") Integer id);
}