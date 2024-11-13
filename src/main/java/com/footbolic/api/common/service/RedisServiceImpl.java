package com.footbolic.api.common.service;

import com.footbolic.api.chat.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    private static final int MAX_MESSAGE_COUNT = 100;

    private static final long ONE_HOUR_IN_MILLIS = 3600000L;

    private final RedisTemplate<String, ChatMessageDto> redisTemplate;

    // 메시지 저장
    @Override
    public void addMessage(ChatMessageDto message) {
        long timestamp = System.currentTimeMillis();
        String key = "chat:room:" + message.getChatroomId();

        // Redis Sorted Set에 추가
        redisTemplate.opsForZSet().add(key, message, timestamp);

        // Redis에서 1시간 초과 메시지 제거
        redisTemplate.opsForZSet().removeRangeByScore(key, 0, timestamp - ONE_HOUR_IN_MILLIS);

        // Redis에서 100개 초과 시 오래된 메시지 삭제
        if (redisTemplate.opsForZSet().size(key) > MAX_MESSAGE_COUNT) {
            redisTemplate.opsForZSet().removeRange(key, 0, -MAX_MESSAGE_COUNT - 1);
        }
    }

    @Override
    public List<ChatMessageDto> getHistory(String chatroomId) {
        String key = "chat:room:" + chatroomId;
        long timestamp = System.currentTimeMillis();

        // Redis에서 조회
        Set<ChatMessageDto> messages = redisTemplate.opsForZSet().range(key, 0, 20);

        // Redis에서 1시간 초과 메시지 제거
        redisTemplate.opsForZSet().removeRangeByScore(key, 0, timestamp - ONE_HOUR_IN_MILLIS);

        // Redis에서 100개 초과 시 오래된 메시지 삭제
        if (redisTemplate.opsForZSet().size(key) > MAX_MESSAGE_COUNT) {
            redisTemplate.opsForZSet().removeRange(key, 0, -MAX_MESSAGE_COUNT - 1);
        }

        if (messages != null && !messages.isEmpty()) {
            return messages.stream().toList();
        } else {
            return null;
        }
    }
}
