package com.footbolic.api.chat.dto;

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

}
