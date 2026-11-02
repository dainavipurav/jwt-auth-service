package com.learning.springboot.authservice.repository;

import com.learning.springboot.authservice.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByTokenHash(String tokenHash);

    @Modifying
    @Transactional
    @Query("""
        DELETE FROM RefreshTokenEntity r
        WHERE r.expiresAt < :currentTime
    """)
    int deleteExpiredTokens(
            @Param("currentTime") LocalDateTime currentTime
    );
}
