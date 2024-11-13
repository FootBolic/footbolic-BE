package com.footbolic.api.chat.service;

import com.footbolic.api.chat.document.ChatMessageDocument;
import com.footbolic.api.chat.dto.ChatMessageDto;
import com.footbolic.api.chat.repository.ChatMessageRepository;
import com.footbolic.api.common.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatMessageRepository chatMessageRepository;

    private final RedisService redisService;

    @Override
    public void insert(ChatMessageDto chatMessage) {
        ChatMessageDto saved = chatMessageRepository.insert(chatMessage.toDocument()).toDto();
        chatMessage.setId(saved.getId());
        redisService.addMessage(chatMessage);
    }

    @Override
    public List<ChatMessageDto> getHistory(String chatroomId, String beforeChatId) {
        List<ChatMessageDto> cached = redisService.getHistory(chatroomId);

        if (cached == null || cached.size() < 20) {
            return chatMessageRepository.getHistory(chatroomId, beforeChatId, 20)
                    .stream().map(ChatMessageDocument::toDto).toList();
        } else {
            return cached;
        }
    }
}
