package com.footbolic.api.common.service;

import com.footbolic.api.chat.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisPubService {

    private final RedisTemplate<String, String> redisTemplate;

    /**
     * Redis publish
     */
    public void publish(ChannelTopic topic, ChatMessageDto dto) {
        redisTemplate.convertAndSend(topic.getTopic(), dto);
    }
}
