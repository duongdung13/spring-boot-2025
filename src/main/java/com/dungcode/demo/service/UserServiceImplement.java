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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

import static java.lang.Thread.sleep;

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

    @Override
    @Transactional
    public ApiResponse<?> transfer(Long fromUserId, Long toUserId, Long amount) {
        User from = userRepository.findByIdForUpdate(fromUserId);
        User to = userRepository.findByIdForUpdate(toUserId);

        if (from.getBalance().compareTo(BigDecimal.valueOf(amount)) < 0) {
            throw new RuntimeException("Người chuyển tiền không đủ số dư");
        }

        from.setBalance(from.getBalance().subtract(BigDecimal.valueOf(amount)));
        to.setBalance(to.getBalance().add(BigDecimal.valueOf(amount)));

        userRepository.save(from);
        userRepository.save(to);

        return new SuccessResponse<>("Transfer success");
    }
      
      
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public ApiResponse<?> demoUpdateBalance() {
        User userFrom = userRepository.findById(3L).orElseThrow();
        User userTo = userRepository.findById(2L).orElseThrow();

        userFrom.setBalance(userFrom.getBalance().subtract(BigDecimal.valueOf(1000)));
        userTo.setBalance(userTo.getBalance().add(BigDecimal.valueOf(1000)));

        // Giảm tiền tài khoản nguồn
        System.out.println("Giảm tiền tài khoản nguồn");
        userRepository.save(userFrom);

        System.out.println("!!! Đoạn này ở request khác có thể lấy ra được thông tin của userFrom sau khi giảm tiền nhưng chưa được commit, có thể gây dirty read");

        System.out.println("Thêm tiền tài khoản đích");
        userRepository.save(userTo);

        return new SuccessResponse<>("Update balance success");
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ApiResponse<?> demoUpdateBalance2() {
        User userFrom = userRepository.findById(3L).orElseThrow();
        System.out.println("Balance of userFrom: " + userFrom.getBalance());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // ✅ THÊM DÒNG NÀY để clear cache
        entityManager.clear();

        User userFrom2 = userRepository.findById(3L).orElseThrow();
        System.out.println("Balance after update: " + userFrom2.getBalance());

        return new SuccessResponse<>("Get balance success");
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public ApiResponse<?> demoUpdateBalance3() {
        User userFrom = userRepository.findById(3L).orElseThrow();
        System.out.println("Balance of userFrom: " + userFrom.getBalance());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // ✅ THÊM DÒNG NÀY để clear cache
        entityManager.clear();

        User userFrom2 = userRepository.findById(3L).orElseThrow();
        System.out.println("Balance after update: " + userFrom2.getBalance());

        return new SuccessResponse<>("Get balance success");
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ApiResponse<?> demoUpdateBalance4() {
        return new SuccessResponse<>("Get balance success");
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
    public ApiResponse<?> getBalanceRealTime() {

        User user = userRepository.findById(3L).orElseThrow();


        return new SuccessResponse<>(user.getBalance(), "Get balance success", 200);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public ApiResponse<?> demoTransferMoney() {
        // Code validate input this here
        // ....

        // Find accounts với lock
        User fromUser = userRepository.findByAccountNumberForUpdate(3)
                .orElseThrow(() -> new GlobalExceptionHandler.NotFoundException("From account not found"));

        User toUser = userRepository.findByAccountNumberForUpdate(2)
                .orElseThrow(() -> new GlobalExceptionHandler.NotFoundException("To account not found"));

        fromUser.setBalance(fromUser.getBalance().subtract(BigDecimal.valueOf(1000)));
        toUser.setBalance(toUser.getBalance().add(BigDecimal.valueOf(1000)));


        return new SuccessResponse<>("Transfer money success");
    }

}
