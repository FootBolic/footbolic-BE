package com.footbolic.api.chat.repository;

import com.footbolic.api.chat.document.ChatMessageDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatMessageRepositoryCustomImpl implements ChatMessageRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public List<ChatMessageDocument> getHistory(String chatroomId, String beforeChatId, int resultSize) {
        Criteria criteria = Criteria.where("chatroomId").is(chatroomId);
        Query query = new Query(criteria)
                .with(Sort.by(Sort.Direction.DESC, "sent_at"))
                .limit(resultSize);

        return mongoTemplate.find(query, ChatMessageDocument.class, "chatMessageDocument");
    }
}
