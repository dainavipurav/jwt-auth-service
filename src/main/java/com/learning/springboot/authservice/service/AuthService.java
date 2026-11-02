package com.learning.springboot.authservice.service;

import com.learning.springboot.authservice.dtos.auth.login.LoginRequestDto;
import com.learning.springboot.authservice.dtos.auth.login.LoginResponseDto;
import com.learning.springboot.authservice.dtos.auth.register.RegisterRequestDto;
import com.learning.springboot.authservice.dtos.success.SuccessResponseDto;

public interface AuthService {

    SuccessResponseDto register(RegisterRequestDto request);

    LoginResponseDto login(LoginRequestDto request);

    SuccessResponseDto logout(String request);
}
