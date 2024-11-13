package com.footbolic.api.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.footbolic.api.chat.document.ChatMessageDocument;
import com.footbolic.api.chat.dto.ChatMessageDto;
import com.footbolic.api.common.service.ChatMessageSubscriber;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final SimpMessagingTemplate messagingTemplate;

    private final ObjectMapper objectMapper;

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    @Bean
    public RedisTemplate<String, ChatMessageDto> redisTemplate() {
        RedisTemplate<String, ChatMessageDto> redisTemplate = new RedisTemplate<>();

        // Redis를 연결합니다.
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        // ObjectMapper 설정
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());  // JavaTimeModule 등록
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.activateDefaultTyping(                 // ChatMessageDocument/Dto 하위 객체만 직렬화, 역직렬화
                BasicPolymorphicTypeValidator.builder()
                        .allowIfSubType(ChatMessageDocument.class)
                        .allowIfSubType(ChatMessageDto.class)   //
                        .allowIfSubType(Set.class)          // SortedSet 사용하기 때문에 허용
                        .build(),
                ObjectMapper.DefaultTyping.NON_FINAL
        );

        // GenericJackson2JsonRedisSerializer에 ObjectMapper 설정
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        // RedisTemplate에 Serializer 설정
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(serializer);

        return redisTemplate;
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListener() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());
        return container;
    }

    @Bean
    public MessageListenerAdapter messageListener() {
        // 메시지 처리 메서드를 정의한 리스너 어댑터 생성
        return new MessageListenerAdapter(
                new ChatMessageSubscriber(messagingTemplate, objectMapper),
                "onMessage"
        ) {
            @Override
            public void onMessage(Message message, byte[] pattern) {
                String body = new String(message.getBody());
                invokeListenerMethod("onMessage", new Object[]{body});
            }
        };
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory,
                                                                       MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        // "chatroom:*" 패턴을 사용하여 모든 채팅방의 채널을 구독
        container.addMessageListener(listenerAdapter, new PatternTopic("chatroom:*"));

        return container;
    }
}