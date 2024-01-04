package com.api.trip.domain.email.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class EmailRedisRepository {

    private final StringRedisTemplate redisTemplate;
    private final static Duration EMAIL_TOKEN_TTL = Duration.ofMinutes(30);

    public void setToken(String email, String token) {
        String key = getKey(email);
        log.debug("set EmailAuth to Redis {}, {}", key, token);
        redisTemplate.opsForValue().set(key, token, EMAIL_TOKEN_TTL);
    }

    public Optional<String> getToken(String email) {
        String key = getKey(email);
        String token = redisTemplate.opsForValue().get(key);
        log.info("get Data from Redis {}, {}", key, token);
        return Optional.ofNullable(token);
    }

    public void deleteToken(String email) {
        String key = getKey(email);
        log.info("delete Data from Redis {}", key);
        redisTemplate.delete(key);
    }

    public boolean existToken(String email) {
        String key = getKey(email);
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    private String getKey(String email) {
        return "EMAIL:" + email;
    }

}
