package com.dungcode.demo.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationRequest {
    @NotNull()
    @Size(min = 3)
    String username;

    @NotNull()
    @Size(min = 3)
    String password;
}
