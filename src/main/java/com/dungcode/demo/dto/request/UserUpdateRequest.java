package com.dungcode.demo.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    String password;

    @NotNull()
    String firstName;

    @NotNull()
    String lastName;

    @NotNull()
    String address;

    @NotNull()
    LocalDate dob;
}
