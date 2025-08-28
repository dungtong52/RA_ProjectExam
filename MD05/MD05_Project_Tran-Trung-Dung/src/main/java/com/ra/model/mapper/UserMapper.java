package com.ra.model.mapper;

import com.ra.model.dto.UserRegisterRequest;
import com.ra.model.dto.UserResponse;
import com.ra.model.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toModel(UserRegisterRequest request) {
        return User.builder()
                .name(request.getName())
                .dob(request.getDob())
                .email(request.getEmail())
                .sex(request.getSex())
                .phone(request.getPhone())
                .password(request.getPassword())
                .build();
    }

    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .dob(user.getDob())
                .email(user.getEmail())
                .sex(user.getSex())
                .phone(user.getPhone())
                .createAt(user.getCreateAt())
                .role(user.getRole())
                .status(user.getStatus())
                .build();
    }
}
