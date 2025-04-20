package com.dungcode.demo.mapper;

import com.dungcode.demo.dto.request.UserCreateRequest;
import com.dungcode.demo.dto.response.UserResponse;
import com.dungcode.demo.posgresql.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUserRequest(UserCreateRequest request);

    UserResponse toUserResponse(User user);

//    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
