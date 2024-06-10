package com.footbolic.api.member.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.footbolic.api.member.entity.MemberEntity;
import com.footbolic.api.role.dto.RoleDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Schema(name = "Member 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {

    private String id;

    @Builder.Default
    private List<RoleDto> roles = new ArrayList<>();

    private String nickname;

    private String idAtProvider;

    private String platform;

    private String refreshToken;

    @DateTimeFormat(pattern="yyyy-MM-ddTHH:mm:ss")
    private LocalDateTime refreshTokenExpiresAt;

    private LocalDateTime nicknameLastUpdatedAt;

    private String accessToken;

    @DateTimeFormat(pattern="yyyy-MM-ddTHH:mm:ss")
    private LocalDateTime accessTokenExpiresAt;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdAt;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updatedAt;

    public MemberEntity toEntity() {
        return MemberEntity.builder()
                .id(id)
                .roles(roles == null ? null : roles.stream().map(RoleDto::toEntity).toList())
                .nickname(nickname)
                .idAtProvider(idAtProvider)
                .platform(platform)
                .refreshToken(refreshToken)
                .refreshTokenExpiresAt(refreshTokenExpiresAt)
                .nicknameLastUpdatedAt(nicknameLastUpdatedAt)
                .accessToken(accessToken)
                .accessTokenExpiresAt(accessTokenExpiresAt)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

    public MemberDto toPublicDto() {
        return MemberDto.builder()
                .id(id)
                .nickname(nickname)
                .build();
    }

}
