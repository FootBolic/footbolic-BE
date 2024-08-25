package com.footbolic.api.chat.repository;

import com.footbolic.api.chat.document.ChatMessageDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessageDocument, String> {
}
