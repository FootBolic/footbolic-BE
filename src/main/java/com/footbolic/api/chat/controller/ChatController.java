package com.footbolic.api.chat.controller;

import com.footbolic.api.chat.dto.ChatMessageDto;
import com.footbolic.api.common.entity.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessageSendingOperations template;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/chat")
    public SuccessResponse getChatMessages(){
        return new SuccessResponse(null);
    }

    @MessageMapping("/message")
    public ResponseEntity<Void> receiveMessage(@RequestBody ChatMessageDto chat) {
        template.convertAndSend("/sub/chatroom", chat);
        return ResponseEntity.ok().build();
    }
}
