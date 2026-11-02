package com.learning.springboot.authservice.service.impl;

import com.learning.springboot.authservice.dtos.auth.login.LoginRequestDto;
import com.learning.springboot.authservice.dtos.auth.login.LoginResponseDto;
import com.learning.springboot.authservice.dtos.auth.register.RegisterRequestDto;
import com.learning.springboot.authservice.dtos.success.SuccessResponseDto;
import com.learning.springboot.authservice.entity.RefreshTokenEntity;
import com.learning.springboot.authservice.entity.RoleEntity;
import com.learning.springboot.authservice.entity.UserEntity;
import com.learning.springboot.authservice.exception.UserAlreadyExistsException;
import com.learning.springboot.authservice.exception.UserNotFoundException;
import com.learning.springboot.authservice.repository.RefreshTokenRepository;
import com.learning.springboot.authservice.repository.RolesRepository;
import com.learning.springboot.authservice.repository.UserRepository;
import com.learning.springboot.authservice.security.JwtService;
import com.learning.springboot.authservice.security.RefreshTokenService;
import com.learning.springboot.authservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RolesRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional
    public SuccessResponseDto register(RegisterRequestDto request) {

        // 1. Check username
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        // 2. Check email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        RoleEntity userRole = roleRepository.findByRole("USER")
                .orElseThrow(() -> new IllegalArgumentException("Default user role not found."));

        UserEntity userEntity = UserEntity.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(true)
                .build();

        userEntity.getRoles().add(userRole);

        userRepository.save(userEntity);

        return SuccessResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message("User registered successfully")
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    @Transactional
    public LoginResponseDto login(LoginRequestDto request) {

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        );

        Authentication authenticated = authenticationManager.authenticate(authentication);

        String username = authenticated.getName();

        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Set<String> roles = userEntity.getRoles().stream().map(RoleEntity::getRole).collect(Collectors.toSet());

        String accessToken = jwtService.generateAccessToken(userEntity);

        return LoginResponseDto.builder()
                .status(HttpStatus.OK.value())
                .accessToken(accessToken)
                .refreshToken(refreshTokenService.createRefreshToken(userEntity))
                .message("Login successful")
                .userId(userEntity.getId())
                .username(username)
                .email(userEntity.getEmail())
                .roles(roles)
                .tokenType("Bearer")
                .expiresIn(jwtService.getAccessTokenExpirationSeconds())
                .build();
    }

    @Override
    public SuccessResponseDto logout(String request) {

        String tokenHash = refreshTokenService.hashToken(request);

        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByTokenHash(tokenHash).orElseThrow(() -> new UserNotFoundException("User not found"));

        refreshTokenEntity.setRevoked(true);

        refreshTokenRepository.save(refreshTokenEntity);

        return SuccessResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message("User logout successfully")
                .timestamp(LocalDateTime.now())
                .build();
    }
}
