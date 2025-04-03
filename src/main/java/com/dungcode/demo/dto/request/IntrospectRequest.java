package com.dungcode.demo.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
public class IntrospectRequest {
    @NotNull(message = "token is not null")
    String token;
}
