package com.dungcode.demo.service;

import com.dungcode.demo.common.ApiResponse;
import com.dungcode.demo.dto.request.UserCreateRequest;
import com.dungcode.demo.dto.request.UserUpdateRequest;

public interface UserService {
    public ApiResponse<?> createUser(UserCreateRequest request);

    public ApiResponse<?> getMyInfo();

    public ApiResponse<?> getUser(Long id);

    public ApiResponse<?> getUsers(int page, int size);

    public ApiResponse<?> updateUser(Long userId, UserUpdateRequest request);

    public ApiResponse<?> deleteUser(Long userId);

    public ApiResponse<?> customQuery();

    public ApiResponse<?> criteriaQuery();

    public ApiResponse<?> transfer(Long fromUserId, Long toUserId, Long amount);

    public ApiResponse<?> demoUpdateBalance();

    public ApiResponse<?> demoUpdateBalance2();

    public ApiResponse<?> demoUpdateBalance3();

    public ApiResponse<?> demoUpdateBalance4();

    public ApiResponse<?> demoTransferMoney();

    public ApiResponse<?> getBalanceRealTime();
}
