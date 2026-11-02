package com.learning.springboot.authservice.dtos.auth.login;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDto {
    private int status;
    private String message;

    private Long userId;
    private String username;
    private String email;
    private Set<String> roles;


    private String accessToken;
    private String tokenType;
    private long expiresIn;
    private String refreshToken;
}
