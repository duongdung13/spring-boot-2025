package com.dungcode.demo.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostRequest {
    @NotNull(message = "Title is not null")
    String title;

    @NotNull(message = "Content is not null")
    String content;

}
