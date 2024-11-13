package com.footbolic.api.chat.repository;

import com.footbolic.api.chat.document.ChatMessageDocument;

import java.util.List;

public interface ChatMessageRepositoryCustom {
    List<ChatMessageDocument> getHistory(String chatroomId, String beforeChatId, int resultSize);
}
