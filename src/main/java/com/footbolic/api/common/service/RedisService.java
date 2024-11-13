package com.footbolic.api.common.service;

import com.footbolic.api.chat.dto.ChatMessageDto;

import java.util.List;

public interface RedisService {
    public void addMessage(ChatMessageDto message) throws RuntimeException;

    public List<ChatMessageDto> getHistory(String chatroomId);
}
