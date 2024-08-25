package com.footbolic.api;

import com.footbolic.api.chat.dto.ChatMessageDto;
import com.footbolic.api.chat.service.ChatService;
import com.footbolic.api.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test/public")
@RequiredArgsConstructor
public class TestController {

    private final RedisUtil redisUtil;

    private final ChatService chatService;

    @GetMapping("/set/{key}/{value}")
    public String set(
            @PathVariable(name = "key") String key,
            @PathVariable(name = "value") String value
    ) {
        redisUtil.setValue(key, value);
        return "saved value for " + key;
    }

    @GetMapping("/get/{key}")
    public String get(
            @PathVariable(name = "key") String key
    ) {
        return redisUtil.getValue(key);
    }

    @GetMapping("/messages")
    public List<ChatMessageDto> chats() {
        return chatService.getMessages();
    }
}
