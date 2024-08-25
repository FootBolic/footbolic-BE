package com.footbolic.api.chat.service;

import com.footbolic.api.chat.document.ChatMessageDocument;
import com.footbolic.api.chat.dto.ChatMessageDto;
import com.footbolic.api.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatMessageRepository chatMessageRepository;

    @Override
    public void insert(ChatMessageDto chatMessage) {
        chatMessageRepository.insert(chatMessage.toDocument());
    }

    @Override
    public List<ChatMessageDto> getMessages() {
        return chatMessageRepository.findAll()
                .stream().map(ChatMessageDocument::toDto)
                .toList();
    }
}
