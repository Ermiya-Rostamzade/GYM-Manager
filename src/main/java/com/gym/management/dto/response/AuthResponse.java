package com.gym.management.dto.response;

//پاسخ احراز هویت
public record AuthResponse (
        String token,
        String tokenType,
        UserResponse user
){
    public AuthResponse (String token, UserResponse user){
        this(token,"Bearer",user);
    }
}
