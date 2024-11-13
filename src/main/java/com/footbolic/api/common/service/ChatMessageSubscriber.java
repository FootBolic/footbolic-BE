package com.footbolic.api.common.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.footbolic.api.chat.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatMessageSubscriber {

    private final SimpMessagingTemplate messagingTemplate;

    private final ObjectMapper objectMapper;

    // Redis로부터 메시지를 수신하면 호출되는 메서드
    public void onMessage(String message, String channel) {
        try {
            // 메시지를 ChatMessageDto로 역직렬화
            ChatMessageDto chatMessageDto = objectMapper.readValue(message, ChatMessageDto.class);

            // STOMP 채널로 ChatMessageDto 객체 전송
            messagingTemplate.convertAndSend("/sub/chat/" + chatMessageDto.getChatroomId(), chatMessageDto);

        } catch (Exception e) {
            log.error("메세지 처리 중 에러가 발생했습니다.", e);
        }
    }
}
