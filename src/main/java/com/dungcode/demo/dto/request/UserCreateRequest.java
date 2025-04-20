package com.dungcode.demo.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreateRequest {
    @Size(min = 3, message = "USERNAME_INVALID")
    @NotNull()
    String username;

    @Size(min = 3, message = "INVALID_PASSWORD")
    @NotNull()
    String password;

    @NotNull()
    String firstName;

    @NotNull()
    String lastName;

    @NotNull()
    LocalDate dob;
}
