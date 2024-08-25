package com.footbolic.api.chat.controller;

import com.footbolic.api.chat.dto.ChatMessageDto;
import com.footbolic.api.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/ws/chat/{chatroomId}")
    @SendTo("/sub/chat/{chatroomId}")
    public ChatMessageDto receiveMessage(
            @RequestBody ChatMessageDto chat,
            @DestinationVariable String chatroomId,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        ChatMessageDto chatMessage = ChatMessageDto.builder()
                .chatroomId(chatroomId)
                .sentFrom(headerAccessor.getSessionAttributes().get("nickname").toString())
                .sentAt(LocalDateTime.now())
                .payload(chat.getPayload())
                .isNotice(false)
                .build();

        chatService.insert(chatMessage);

        return chatMessage;
    }

    @MessageMapping("/ws/chat/enter/{chatroomId}")
    @SendTo("/sub/chat/{chatroomId}")
    public ChatMessageDto onEnter(
            @RequestBody ChatMessageDto chat,
            @DestinationVariable String chatroomId,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        String nickname = headerAccessor.getSessionAttributes().get("nickname").toString();
        ChatMessageDto chatMessage = ChatMessageDto.builder()
                .chatroomId(chatroomId)
                .sentFrom(nickname)
                .sentAt(LocalDateTime.now())
                .payload(nickname + " 님이 입장하셨습니다.")
                .isNotice(true)
                .build();

        chatService.insert(chatMessage);

        return chatMessage;
    }
    
    @MessageMapping("/ws/chat/leave/{chatroomId}")
    @SendTo("/sub/chat/{chatroomId}")
    public ChatMessageDto onLeave(
            @RequestBody ChatMessageDto chat,
            @DestinationVariable String chatroomId,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        String nickname = headerAccessor.getSessionAttributes().get("nickname").toString();
        ChatMessageDto chatMessage = ChatMessageDto.builder()
                .chatroomId(chatroomId)
                .sentFrom(nickname)
                .sentAt(LocalDateTime.now())
                .payload(nickname + " 님이 퇴장하셨습니다.")
                .isNotice(true)
                .build();

        chatService.insert(chatMessage);

        return chatMessage;
    }
}
