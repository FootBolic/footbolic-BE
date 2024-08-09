package com.footbolic.api.chat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {

    private String sentFrom;

    private String sentAt;

    private String payload;

    @JsonProperty("isNotice")
    private boolean isNotice;

}
