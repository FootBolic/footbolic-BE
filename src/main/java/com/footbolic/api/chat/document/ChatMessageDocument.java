package com.footbolic.api.chat.document;

import com.footbolic.api.chat.dto.ChatMessageDto;
import jakarta.persistence.Id;
import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collation = "chat_message")
@Builder
public class ChatMessageDocument {

    @Id
    private String id;

    @Field(name = "room_id")
    private String chatroomId;

    @Field(name = "sent_from")
    private String sentFrom;

    @Field(name = "sent_at")
    private LocalDateTime sentAt;

    @Field(name = "payload")
    private String payload;

    @Field(name = "is_notice")
    private boolean isNotice;

    public ChatMessageDto toDto() {
        return ChatMessageDto.builder()
                .id(id)
                .chatroomId(chatroomId)
                .sentFrom(sentFrom)
                .sentAt(sentAt)
                .payload(payload)
                .isNotice(isNotice)
                .build();
    }
}
