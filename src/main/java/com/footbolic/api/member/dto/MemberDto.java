package com.footbolic.api.member.dto;

import com.footbolic.api.member.entity.MemberEntity;
import com.footbolic.api.role.dto.RoleDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

@Schema(name = "Member 객체")
@Data
@Builder
public class MemberDto {

    private String id;

    private String roleId;

    private RoleDto role;

    private String nickname;

    private String idAtProvider;

    private String platform;

    private String refreshToken;

    @DateTimeFormat(pattern="yyyy-MM-ddTHH:mm:ss")
    private LocalDateTime refreshTokenExpiresAt;

    private String accessToken;

    @DateTimeFormat(pattern="yyyy-MM-ddTHH:mm:ss")
    private LocalDateTime accessTokenExpiresAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public MemberEntity toEntity() {
        return MemberEntity.builder()
                .id(id)
                .roleId(roleId)
                .role(role == null ? null : role.toEntity())
                .nickname(nickname)
                .idAtProvider(idAtProvider)
                .platform(platform)
                .refreshToken(refreshToken)
                .refreshTokenExpiresAt(refreshTokenExpiresAt)
                .accessToken(accessToken)
                .accessTokenExpiresAt(accessTokenExpiresAt)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

}
