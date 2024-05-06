package com.footbolic.api.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.footbolic.api.common.entity.BaseEntity;
import com.footbolic.api.member.dto.MemberDto;
import com.footbolic.api.notification.entity.NotificationEntity;
import com.footbolic.api.role.entity.RoleEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@SuperBuilder
@NoArgsConstructor
@Entity(name = "MemberEntity")
@Table(name = "Member")
public class MemberEntity extends BaseEntity {

    @Column(name = "role_id", nullable = false, length = 30)
    private String roleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    private RoleEntity role;

    @Column(name = "nickname", nullable = false, length = 20)
    private String nickname;

    @Column(name = "id_at_provider", length = 100)
    private String idAtProvider;

    @Column(name = "platform", nullable = false, updatable = false, length = 20)
    private String platform;

    @Column(name = "refresh_token", length = 200)
    private String refreshToken;

    @Column(name = "refresh_token_expires_at")
    private LocalDateTime refreshTokenExpiresAt;

    @Column(name = "access_token", length = 200)
    private String accessToken;

    @Column(name = "access_token_expires_at")
    private LocalDateTime accessTokenExpiresAt;

    @Column(name = "id_token", length = 2000)
    private String idToken;

    @Column(name = "scope", length = 30)
    private String scope;

    @Column(name = "token_type", length = 30)
    private String tokenType;

    @Builder.Default
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<NotificationEntity> notifications = new ArrayList<>();

    public MemberDto toDto() {
        return MemberDto.builder()
                .id(getId())
                .roleId(roleId)
                .role(role == null ? null : role.toDto())
                .nickname(nickname)
                .idAtProvider(idAtProvider)
                .platform(platform)
                .refreshToken(refreshToken)
                .refreshTokenExpiresAt(refreshTokenExpiresAt)
                .accessToken(accessToken)
                .accessTokenExpiresAt(accessTokenExpiresAt)
                .idToken(idToken)
                .scope(scope)
                .tokenType(tokenType)
                .createdAt(getCreatedAt())
                .updatedAt(getUpdatedAt())
                .build();
    }

}
