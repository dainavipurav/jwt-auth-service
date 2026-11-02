package com.learning.springboot.authservice.scheduler;

import com.learning.springboot.authservice.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExpiredTokenCleanupScheduler {
    private final RefreshTokenRepository refreshTokenRepository;

    @Scheduled(fixedRate = 60000)
    public void removeUnusedTokens(){
        int totalDeletedRows = refreshTokenRepository.deleteExpiredTokens(LocalDateTime.now());

        log.info("Total expired tokens deleted rows count : " + totalDeletedRows);
    }
}
