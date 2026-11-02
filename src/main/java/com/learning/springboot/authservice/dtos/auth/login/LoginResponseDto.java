package com.learning.springboot.authservice.dtos.auth.login;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDto {
    private int status;
    private String message;
    private String accessToken;
    private String refreshToken;
}
