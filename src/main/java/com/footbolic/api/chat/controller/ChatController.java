package com.footbolic.api.chat.controller;

import com.footbolic.api.chat.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {

    @MessageMapping("/ws/chat")
    @SendTo("/sub/chat")
    public ChatMessageDto receiveMessage(@RequestBody ChatMessageDto chat, SimpMessageHeaderAccessor headerAccessor) {
       chat.setSentFrom(headerAccessor.getSessionAttributes().get("nickname").toString());
        return chat;
    }

    @MessageMapping("/ws/chat/enter")
    @SendTo("/sub/chat")
    public ChatMessageDto onEnter(@RequestBody ChatMessageDto chat, SimpMessageHeaderAccessor headerAccessor) {
        String nickname = headerAccessor.getSessionAttributes().get("nickname").toString();
        chat.setSentFrom(nickname);
        chat.setPayload(nickname + " 님이 입장하셨습니다.");
        chat.setNotice(true);
        return chat;
    }
    
    @MessageMapping("/ws/chat/leave")
    @SendTo("/sub/chat")
    public ChatMessageDto onLeave(@RequestBody ChatMessageDto chat, SimpMessageHeaderAccessor headerAccessor) {
        String nickname = headerAccessor.getSessionAttributes().get("nickname").toString();
        chat.setSentFrom(nickname);
        chat.setPayload(nickname + " 님이 퇴장하셨습니다.");
        chat.setNotice(true);
        return chat;
    }
}
