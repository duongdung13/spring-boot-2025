package com.dungcode.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    Long id;
    String username;
    String firstName;
    String lastName;
    String address;
    LocalDate dob;
    Set<String> roles;
    private List<AddressResponse> addresses;

}
