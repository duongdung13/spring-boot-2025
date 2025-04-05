package com.dungcode.demo.dto.request;

import com.dungcode.demo.dto.validator.ValidEnum;
import com.dungcode.demo.enums.PostType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
public class PostRequest {
    @NotBlank(message = "Title is not blank")
    String title;

    @NotBlank(message = "Content is not blank")
    String content;


    // Validate enum
//    @NotNull(message = "Post type is invalid")
//    PostType postType;

    // Regex Pattern
    @NotNull(message = "Post type không để trống")
    @Pattern(regexp = "PAGE|POST|NONE", message = "Post type không hợp lệ")
    private String postType;

    // Validate enum kiểu khác
//    @NotNull(message = "Post type is required")
//    @ValidEnum(enumClass = PostType.class, message = "Post type invalid")
//    private String postType;

}
