package com.footbolic.api.chat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.footbolic.api.chat.document.ChatMessageDocument;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {

    private String id;

    private String chatroomId;

    private String sentFrom;

    private LocalDateTime sentAt;

    private String payload;

    @JsonProperty("isNotice")
    private boolean isNotice;

    public ChatMessageDocument toDocument() {
        return ChatMessageDocument.builder()
                .id(id)
                .chatroomId(chatroomId)
                .sentFrom(sentFrom)
                .sentAt(sentAt)
                .payload(payload)
                .isNotice(isNotice)
                .build();
    }
}
