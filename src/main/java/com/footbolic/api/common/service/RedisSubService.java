package com.footbolic.api.common.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.footbolic.api.chat.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisSubService implements MessageListener {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            ChatMessageDto chatMessageDto = mapper.readValue(message.getBody(), ChatMessageDto.class);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
