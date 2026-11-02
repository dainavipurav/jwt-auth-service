package com.learning.springboot.authservice.controller;

import com.learning.springboot.authservice.dtos.auth.login.LoginRequestDto;
import com.learning.springboot.authservice.dtos.auth.login.LoginResponseDto;
import com.learning.springboot.authservice.dtos.auth.register.RegisterRequestDto;
import com.learning.springboot.authservice.dtos.success.SuccessResponseDto;
import com.learning.springboot.authservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public SuccessResponseDto register(@Valid @RequestBody RegisterRequestDto request){
        return authService.register(request);
    }

    @PostMapping("/login")
    public LoginResponseDto login(@Valid @RequestBody LoginRequestDto request){
        return authService.login(request);
    }


    @PostMapping("/logout")
    public SuccessResponseDto Logout(@RequestParam String refreshToken){
        return authService.logout(refreshToken);
    }
}
