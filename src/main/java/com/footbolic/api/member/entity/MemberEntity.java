package com.footbolic.api.member.entity;

import com.footbolic.api.common.entity.BaseEntity;
import com.footbolic.api.member.dto.MemberDto;
import com.footbolic.api.notification.entity.NotificationEntity;
import com.footbolic.api.role.entity.RoleEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@SuperBuilder
@Entity(name = "MemberEntity")
@Table(name = "Member")
public class MemberEntity extends BaseEntity {

    @Column(name = "role_id", nullable = false, length = 30)
    private String roleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    private RoleEntity role;

    @Column(name = "full_name", nullable = false, length = 15)
    private String fullName;

    @Column(name = "nickname", nullable = false, length = 20)
    private String nickname;

    @Column(name = "refresh_token", length = 100)
    private String refreshToken;

    @Builder.Default
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<NotificationEntity> notifications = new ArrayList<>();

    public MemberDto toDto() {
        return MemberDto.builder()
                .id(getId())
                .roleId(roleId)
                .role(role == null ? null : role.toDto())
                .fullName(fullName)
                .nickname(nickname)
                .refreshToken(refreshToken)
                .createdAt(getCreatedAt())
                .updatedAt(getUpdatedAt())
                .build();
    }

}
