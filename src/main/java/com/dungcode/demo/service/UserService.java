package com.dungcode.demo.service;

import com.dungcode.demo.common.ApiResponse;
import com.dungcode.demo.common.SuccessResponse;
import com.dungcode.demo.dto.request.UserCreateRequest;
import com.dungcode.demo.dto.response.UserResponse;
import com.dungcode.demo.enums.Role;
import com.dungcode.demo.exception.GlobalExceptionHandler;
import com.dungcode.demo.mapper.UserMapper;
import com.dungcode.demo.posgresql.entity.User;
import com.dungcode.demo.posgresql.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, UserMapper userMapper) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

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


//    public ApiResponse<?> updateUser(String userId, UserUpdateRequest request) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new BadRequestException("abc"));
//
//        userMapper.updateUser(user, request);
//
//        return new SuccessResponse<>(userRepository.save(user));
//    }


//    public void deleteUser(String userId) {
//        userRepository.deleteById(userId);
//    }


}
