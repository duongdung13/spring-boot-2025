package com.dungcode.demo.controller;

import com.dungcode.demo.dto.request.UserCreateRequest;
import com.dungcode.demo.dto.request.UserUpdateRequest;
import com.dungcode.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Slf4j
@Tag(name = "users Controller", description = "Quản lý danh sách users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody @Valid UserCreateRequest request) {
        return (userService.createUser(request)).responseEntity();
    }

    @GetMapping("/my-info")
    public ResponseEntity<?> getMyInfo() {

        return (userService.getMyInfo()).responseEntity();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@PathVariable("userId") Long userId) {
        return (userService.getUser(userId)).responseEntity();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    @Operation(summary = "Lấy danh sách người dùng", description = "API này trả về danh sách người dùng có phân trang")
    public ResponseEntity<?> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return (userService.getUsers(page, size)).responseEntity();
    }

    @PutMapping("/{userId}")
    ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody @Valid UserUpdateRequest request) {
        return (userService.updateUser(userId, request)).responseEntity();
    }

    @DeleteMapping("/{userId}")
    ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        return (userService.deleteUser(userId)).responseEntity();
    }

    @GetMapping("/custom-query")
    ResponseEntity<?> customQuery() {
        return (userService.customQuery()).responseEntity();
    }

    @GetMapping("/criteria-query")
    ResponseEntity<?> criteriaQuery() {
        return (userService.criteriaQuery()).responseEntity();
    }

    @PostMapping("/demo-update-balance")
    ResponseEntity<?> demoUpdateBalance() {
        return (userService.demoUpdateBalance()).responseEntity();
    }

    @PostMapping("/demo-update-balance-2")
    ResponseEntity<?> demoUpdateBalance2() {
        return (userService.demoUpdateBalance2()).responseEntity();
    }

    @PostMapping("/demo-update-balance-3")
    ResponseEntity<?> demoUpdateBalance3() {
        return (userService.demoUpdateBalance3()).responseEntity();
    }

    @PostMapping("/demo-update-balance-4")
    ResponseEntity<?> demoUpdateBalance4() {
        return (userService.demoUpdateBalance4()).responseEntity();
    }

    @PostMapping("/demo-transfer-money")
    ResponseEntity<?> demoTransferMoney() {
        return (userService.demoTransferMoney()).responseEntity();
    }

    @PostMapping("/get-balance-real-time")
    ResponseEntity<?> getBalanceRealTime() {
        return (userService.getBalanceRealTime()).responseEntity();
    }

}

