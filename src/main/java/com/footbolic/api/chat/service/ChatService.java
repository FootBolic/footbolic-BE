package com.footbolic.api.chat.service;

import com.footbolic.api.chat.dto.ChatMessageDto;

import java.util.List;

public interface ChatService {

    void insert(ChatMessageDto chatMessage);

    List<ChatMessageDto> getMessages();
}
