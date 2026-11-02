package com.learning.springboot.authservice.security;

import com.learning.springboot.authservice.entity.RoleEntity;
import com.learning.springboot.authservice.entity.UserEntity;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class JwtService {
    private final SecretKey signingKey;
    private final long accessTokenExpirationMs;

    public JwtService(
     @Value("${jwt.secret}") String secret,
     @Value("${jwt.access-token-expiration-ms}") long accessTokenExpiry
    ){
        this.signingKey = Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(secret)
        );

        this.accessTokenExpirationMs = accessTokenExpiry;
    }

    public String generateAccessToken(UserEntity entity){
        Date issuedAt = new Date();

        Date expiration = new Date(issuedAt.getTime() + accessTokenExpirationMs);

        List<String> roles = entity.getRoles()
                .stream()
                .map(RoleEntity::getRole)
                .toList();

        return Jwts.builder()
                .signWith(signingKey)
                .issuedAt(issuedAt)
                .expiration(expiration)
                .subject(entity.getUsername())
                .claims(Map.of("userId", entity.getId(),"roles", roles))
                .compact();
    }

    public long getAccessTokenExpirationSeconds() {
        return accessTokenExpirationMs / 1000;
    }
}
