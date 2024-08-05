package com.footbolic.api.handler;

import com.footbolic.api.member.dto.MemberDto;
import com.footbolic.api.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class StompHandler implements ChannelInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if (StompCommand.CONNECT == accessor.getCommand()) {
            String authorization = accessor.getFirstNativeHeader("Authorization");
            if (authorization != null && authorization.length() > 7) {
                String accessToken = authorization.substring(7);

                if (!jwtUtil.validateAccessToken(accessToken)) {
                    throw new MessageDeliveryException("유효하지 않은 토큰입니다.");
                }

                MemberDto member = jwtUtil.resolveAccessToken(accessToken);
                accessor.getSessionAttributes().put("nickname", member.getNickname());
            } else {
                throw new MessageDeliveryException("유효하지 않은 토큰입니다.");
            }
        }
        return message;
    }
}