package com.footbolic.api.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisUtil {

    private final RedisTemplate<String, String> redisTemplate;

    /**
     * redis 저장소에 값을 저장한다.
     * @param key {String} redis에 값을 저장할 key
     * @param value {String} redis에 저장할 값
     */
    public void setValue(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * redis 저장소에 key로 저장된 value를 조회한다.
     * @param key {String} 조회하려는 key
     * @return key에 저장된 value
     */
    public String getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}
