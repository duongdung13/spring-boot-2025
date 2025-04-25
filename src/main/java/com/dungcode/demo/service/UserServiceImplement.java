package com.dungcode.demo.service;

import com.dungcode.demo.common.ApiResponse;
import com.dungcode.demo.common.SuccessResponse;
import com.dungcode.demo.dto.request.UserCreateRequest;
import com.dungcode.demo.dto.request.UserUpdateRequest;
import com.dungcode.demo.enums.Role;
import com.dungcode.demo.exception.GlobalExceptionHandler;
import com.dungcode.demo.mapper.UserMapper;
import com.dungcode.demo.posgresql.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.dungcode.demo.posgresql.repository.UserRepository;

import java.util.HashSet;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImplement implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

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


    public ApiResponse<?> getUsers() {
        var userAuth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("getName " + userAuth.getName());
        userAuth.getAuthorities().forEach(grantedAuthority -> {
            System.out.println("grantedAuthority: " + grantedAuthority.getAuthority());
        });

        return new SuccessResponse<>(userRepository.findAll().stream().map(this.userMapper::toUserResponse
        ).toList());
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

}
