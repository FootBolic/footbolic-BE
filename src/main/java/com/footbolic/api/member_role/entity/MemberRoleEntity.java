package com.footbolic.api.member_role.entity;

import com.footbolic.api.common.entity.BaseEntity;
import com.footbolic.api.member.entity.MemberEntity;
import com.footbolic.api.member_role.dto.MemberRoleDto;
import com.footbolic.api.role.entity.RoleEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity(name = "MemberRoleEntity")
@Table(name = "Member_Role")
@Getter
@SuperBuilder
@NoArgsConstructor
public class MemberRoleEntity extends BaseEntity {

    @Column(name = "member_id", nullable = false, updatable = false)
    private String memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", insertable = false, updatable = false)
    private MemberEntity member;

    @Column(name = "role_id", nullable = false, updatable = false)
    private String roleId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    private RoleEntity role;

    public MemberRoleDto toDto() {
        return MemberRoleDto.builder()
                .id(getId())
                .memberId(memberId)
                .member(member)
                .roleId(roleId)
                .role(role)
                .createdAt(getCreatedAt())
                .updatedAt(getUpdatedAt())
                .build();
    }
}
