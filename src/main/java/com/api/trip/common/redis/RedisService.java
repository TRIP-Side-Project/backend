package com.api.trip.common.redis;


import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;


    public void setData(String key, Object value, Long time, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value.toString(), time, timeUnit);
    }

    public Object getData(String key) {
        return redisTemplate.opsForValue().get(key);
    }


    public void deleteData(String key) {
        redisTemplate.delete(key);
    }


    public void increaseData(String key) {
        redisTemplate.opsForValue().increment(key);
    }

}
