package com.learning.springboot.authservice.security;

import com.learning.springboot.authservice.entity.RefreshTokenEntity;
import com.learning.springboot.authservice.entity.UserEntity;
import com.learning.springboot.authservice.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HexFormat;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    @Value("${jwt.refresh-expiration}")
    private long refreshTokenExpiry;

    public String createRefreshToken(UserEntity entity){
        byte[] randomByte = new byte[32];

        secureRandom.nextBytes(randomByte);

        String refreshToken = Base64.getUrlEncoder().withoutPadding().encodeToString(randomByte);

        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .user(entity)
                .tokenHash(hashToken(refreshToken))
                .expiresAt(LocalDateTime.now().plusNanos(refreshTokenExpiry * 1_000_000))
                .revoked(false)
                .createdAt(LocalDateTime.now())
                .build();

        refreshTokenRepository.save(refreshTokenEntity);

        return refreshToken;
    }

    public String hashToken(String token) {

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(
                    token.getBytes(StandardCharsets.UTF_8)
            );

            return HexFormat.of().formatHex(hash);

        } catch (Exception exception) {
            throw new IllegalStateException("Unable to hash refresh token", exception);
        }
    }
}
