package com.dungcode.demo.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationRequest {
    @NotNull(message = "Username is not null")
    @Size(min = 3, message = "Username must be at least 3 characters long")
    String username;

    @NotNull(message = "Password is not null")
    @Size(min = 3, message = "Password must be at least 3 characters long")
    String password;
}
