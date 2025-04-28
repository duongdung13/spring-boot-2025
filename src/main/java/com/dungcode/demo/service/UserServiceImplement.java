package com.dungcode.demo.service;

import com.dungcode.demo.common.ApiResponse;
import com.dungcode.demo.common.SuccessResponse;
import com.dungcode.demo.dto.request.UserCreateRequest;
import com.dungcode.demo.dto.request.UserUpdateRequest;
import com.dungcode.demo.dto.response.PageResponse;
import com.dungcode.demo.enums.Role;
import com.dungcode.demo.exception.GlobalExceptionHandler;
import com.dungcode.demo.mapper.UserMapper;
import com.dungcode.demo.posgresql.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.dungcode.demo.posgresql.repository.UserRepository;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImplement implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public ApiResponse<?> createUser(UserCreateRequest request) {
        if (this.userRepository.existsByUsername(request.getUsername())) {
            throw new GlobalExceptionHandler.BadRequestCustomException("Exists by username");
        }

        User user = this.userMapper.toUserRequest(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());
        user.setRoles(roles);

        User saveUser = userRepository.save(user);

        return new SuccessResponse<>(this.userMapper.toUserResponse(saveUser));
    }

    public ApiResponse<?> getMyInfo() {
        var userAuth = SecurityContextHolder.getContext().getAuthentication();
        String username = userAuth.getName();

        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new GlobalExceptionHandler.NotFoundException("Not found 1");
        }

        User user = userOptional.get();

        return new SuccessResponse<>(this.userMapper.toUserResponse(user));
    }

    public ApiResponse<?> getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new GlobalExceptionHandler.NotFoundException("User not found"));

        return new SuccessResponse<>(this.userMapper.toUserResponse(user));
    }

    public ApiResponse<?> getUsers(int page, int size) {
        var userAuth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("getName " + userAuth.getName());
        userAuth.getAuthorities().forEach(grantedAuthority -> {
            System.out.println("grantedAuthority: " + grantedAuthority.getAuthority());
        });

        var pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        var pageResult = userRepository.findAll(pageable);

        var content = pageResult.getContent().stream()
                .map(this.userMapper::toUserResponse)
                .toList();

        var response = new PageResponse<>(
                content,
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.getTotalPages(),
                pageResult.isLast()
        );

        return new SuccessResponse<>(response);
    }

    public ApiResponse<?> updateUser(Long userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GlobalExceptionHandler.NotFoundException("User not found"));

        userMapper.updateUser(user, request);

        return new SuccessResponse<>(this.userMapper.toUserResponse(userRepository.save(user)));
    }

    public ApiResponse<?> deleteUser(Long userId) {
        return new SuccessResponse<>(userRepository.findById(userId)
                .map(user -> {
                    userRepository.delete(user);
                    return "Delete success";
                })
                .orElseThrow(() -> new GlobalExceptionHandler.NotFoundException("User not found")));
    }

    @Override
    public ApiResponse<?> customQuery() {
//        return new SuccessResponse<>(userRepository.customQueryFindByUsername("dung01"));
        //return new SuccessResponse<>(userRepository.customQueryFindActiveUsersByRole(Role.USER.name()));
        return new SuccessResponse<>(userRepository.findAllByUsername("dung02"));
    }

    @Override
    public ApiResponse<?> criteriaQuery() {
        String name = "dung";

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> user = query.from(User.class);

        List<Predicate> predicates = new ArrayList<>();

        if (name != null) {
            predicates.add(cb.like(cb.lower(user.get("username")), "%" + name.toLowerCase() + "%"));
        }

        if (!predicates.isEmpty()) {
            query.where(predicates.toArray(new Predicate[0]));
        }
        query.orderBy(cb.asc(user.get("username")));

        return new SuccessResponse<>(entityManager.createQuery(query).getResultList());
    }

}
