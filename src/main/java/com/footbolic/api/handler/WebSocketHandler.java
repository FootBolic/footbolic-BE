package com.footbolic.api.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private static final ConcurrentHashMap<String, WebSocketSession> CLIENTS = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        CLIENTS.entrySet().forEach(arg -> 
                send(arg.getValue(), toMap(
                        "",
                        Objects.requireNonNull(session.getPrincipal()).getName() + "님이 입장하셨습니다.")
                )
        );
        CLIENTS.put(session.getId(), session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        CLIENTS.remove(session.getId());
        CLIENTS.entrySet().forEach(arg ->
                send(arg.getValue(), toMap(
                        "",
                        Objects.requireNonNull(session.getPrincipal()).getName() + "님이 퇴장하셨습니다.")
                )
        );
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        String id = session.getId();  // 세션 ID
        String senderName = Objects.requireNonNull(session.getPrincipal()).getName();
        CLIENTS.entrySet().forEach( arg-> {
            if (!arg.getKey().equals(id)) send(arg.getValue(), toMap(senderName, message.getPayload()));  // 같은 아이디가 아니면 메시지 전달
        });
    }

    private TextMessage toMap(String sender, String payload) {
        Map<String, String> map = Map.of("sentFrom", sender, "payload", payload);
        ObjectMapper mapper = new ObjectMapper();
        try {
            return new TextMessage(Objects.requireNonNull(mapper.writeValueAsString(map)));
        } catch (IOException e) {
            return new TextMessage(
            "\"sentFrom\": \"" + sender + "\","
                +   "\"payload\": \"" + payload + "\"");
        }
    }

    private void send(WebSocketSession session, TextMessage message) {
        try {
            session.sendMessage(message);
        } catch (IOException e) {
            log.error("{}", e.getMessage());
        }
    }
}