package com.footbolic.api.member.entity;

import com.footbolic.api.common.entity.BaseEntity;
import com.footbolic.api.member.dto.MemberDto;
import com.footbolic.api.member_role.entity.MemberRoleEntity;
import com.footbolic.api.notification.entity.NotificationEntity;
import com.footbolic.api.role.entity.RoleEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Schema(name = "회원 Entity")
@Getter
@SuperBuilder
@NoArgsConstructor
@Entity(name = "MemberEntity")
@Table(name = "Member")
public class MemberEntity extends BaseEntity {

    @Builder.Default
    @OneToMany(mappedBy = "member", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<MemberRoleEntity> memberRoles = new ArrayList<>();

    @Builder.Default()
    @Transient
    private List<RoleEntity> roles = new ArrayList<>();

    @Column(name = "nickname", nullable = false, length = 20)
    private String nickname;

    @Column(name = "id_at_provider", nullable = false, length = 100)
    private String idAtProvider;

    @Column(name = "platform", nullable = false, length = 20)
    private String platform;

    @Column(name = "refresh_token", length = 500)
    private String refreshToken;

    @Column(name = "refresh_token_expires_at")
    private LocalDateTime refreshTokenExpiresAt;

    @Column(name = "nickname_last_updated_at")
    private LocalDateTime nicknameLastUpdatedAt;

    @Transient
    private String accessToken;

    @Transient
    private LocalDateTime accessTokenExpiresAt;

    @Builder.Default
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<NotificationEntity> notifications = new ArrayList<>();

    public MemberDto toDto() {
        return MemberDto.builder()
                .id(getId())
                .roles(memberRoles.stream().map(MemberRoleEntity::getRole).map(RoleEntity::toDto).toList())
                .nickname(nickname)
                .idAtProvider(idAtProvider)
                .platform(platform)
                .refreshToken(refreshToken)
                .refreshTokenExpiresAt(refreshTokenExpiresAt)
                .nicknameLastUpdatedAt(nicknameLastUpdatedAt)
                .accessToken(accessToken)
                .accessTokenExpiresAt(accessTokenExpiresAt)
                .createdAt(getCreatedAt())
                .updatedAt(getUpdatedAt())
                .build();
    }

    public MemberEntity fetchRoles() {
        this.roles = this.memberRoles.stream().map(MemberRoleEntity::getRole).toList();
        return this;
    }

}
