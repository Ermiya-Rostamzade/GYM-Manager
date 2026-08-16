package com.gym.management.mapper;

import com.gym.management.dto.request.UserRegisterRequest;
import com.gym.management.dto.response.UserResponse;
import com.gym.management.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "permissions", ignore = true)
    @Mapping(target = "notifications", ignore = true)
    @Mapping(target = "subscriptions", ignore = true)
    User toEntity(UserRegisterRequest userRegisterRequest);

    UserResponse toResponse(User entity);
}
