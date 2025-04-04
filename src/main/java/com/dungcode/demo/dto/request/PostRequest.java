package com.dungcode.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
public class PostRequest {
    @NotBlank(message = "Title is not blank")
    String title;

    @NotBlank(message = "Content is not blank")
    String content;

}
