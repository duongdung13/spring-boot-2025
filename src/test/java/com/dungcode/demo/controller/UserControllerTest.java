package com.dungcode.demo.controller;

import com.dungcode.demo.common.SuccessResponse;
import com.dungcode.demo.config.SecurityConfig;
import com.dungcode.demo.dto.request.UserCreateRequest;
import com.dungcode.demo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void createUser_ShouldReturnCreatedStatus() throws Exception {
        UserCreateRequest request = new UserCreateRequest();
        request.setUsername("username");
        request.setPassword("password123");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setDob(LocalDate.of(1990, 12, 20));

        when(userService.createUser(any(UserCreateRequest.class)))
                .thenReturn(new SuccessResponse<>("User created successfully"));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createUser_WithInvalidRequest_ShouldReturnBadRequest() throws Exception {
        UserCreateRequest request = new UserCreateRequest();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}