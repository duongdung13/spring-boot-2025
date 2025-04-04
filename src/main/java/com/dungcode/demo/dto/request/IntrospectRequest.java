package com.dungcode.demo.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
public class IntrospectRequest {
    @NotNull(message = "Token is not null")
    String token;
}
