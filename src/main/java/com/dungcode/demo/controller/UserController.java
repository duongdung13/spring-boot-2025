package com.dungcode.demo.controller;

import com.dungcode.demo.dto.request.UserCreateRequest;
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
    @Operation(summary = "Lấy thông tin người dùng", description = "API này trả về thông tin của một người dùng theo ID")
    public ResponseEntity<?> getUsers() {
        return (userService.getUsers()).responseEntity();
    }

//    @PutMapping("/{userId}")
//    ResponseEntity<?> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request) {
//        return (userService.updateUser(userId, request)).responseEntity();
//    }

//    @DeleteMapping("/{userId}")
//    String deleteUser(@PathVariable String userId) {
//        userService.deleteUser(userId);
//        return "User has been deleted";
//    }
}

