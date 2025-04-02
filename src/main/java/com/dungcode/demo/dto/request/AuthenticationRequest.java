package com.dungcode.demo.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationRequest {
    @NotNull(message = "username is not null")
    @Size(min = 3, message = "USERNAME_INVALID")
    String username;

    @NotNull(message = "password is not null")
    @Size(min = 3, message = "INVALID_PASSWORD")
    String password;
}
