package com.dinhlap.ims.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    @Value("${jwt.refresh-expiration-ms}")
    private long refreshExpirationMs;

    private final RedisTemplate<String, String> redisTemplate;
    private static final String REFRESH_TOKEN_PREFIX = "refresh_token:";
    private static final String BLACKLIST_ACCESS_TOKEN_PREFIX = "blacklist_access_token:";

    public void saveRefreshToken(String username, String refreshToken) {
        redisTemplate.opsForValue().set(REFRESH_TOKEN_PREFIX + username, refreshToken, refreshExpirationMs, TimeUnit.MILLISECONDS);
    }

    public String getRefreshToken(String username) {
        return redisTemplate.opsForValue().get(REFRESH_TOKEN_PREFIX + username);
    }

    public void deleteRefreshToken(String username) {
        redisTemplate.delete(REFRESH_TOKEN_PREFIX + username);
    }

    public void blacklistAccessToken(String token, long accessExpirationMs) {
        redisTemplate.opsForValue().set(BLACKLIST_ACCESS_TOKEN_PREFIX + token, "blacklisted", accessExpirationMs, TimeUnit.MILLISECONDS);
    }

    public boolean isAccessTokenBlacklisted(String token) {
        Boolean exists = redisTemplate.hasKey(BLACKLIST_ACCESS_TOKEN_PREFIX + token);

        return exists != null && exists;
    }
}
