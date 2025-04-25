package com.dungcode.demo.service;

import com.dungcode.demo.common.ApiResponse;
import com.dungcode.demo.dto.request.UserCreateRequest;
import com.dungcode.demo.dto.request.UserUpdateRequest;

public interface UserService {
    public ApiResponse<?> createUser(UserCreateRequest request);

    public ApiResponse<?> getMyInfo();

    public ApiResponse<?> getUser(Long id);

    public ApiResponse<?> getUsers();

    public ApiResponse<?> updateUser(Long userId, UserUpdateRequest request);

    public ApiResponse<?> deleteUser(Long userId);
}
