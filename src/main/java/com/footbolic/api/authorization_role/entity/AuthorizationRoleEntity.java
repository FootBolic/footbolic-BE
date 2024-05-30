package com.footbolic.api.authorization_role.entity;

import com.footbolic.api.authorization.entity.AuthorizationEntity;
import com.footbolic.api.authorization_role.dto.AuthorizationRoleDto;
import com.footbolic.api.common.entity.ExtendedBaseEntity;
import com.footbolic.api.role.entity.RoleEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity(name = "AuthorizationRoleEntity")
@Table(name = "Authorization_Role")
@Getter
@SuperBuilder
@NoArgsConstructor
public class AuthorizationRoleEntity extends ExtendedBaseEntity {

    @Column(name = "authorization_id", nullable = false, updatable = false)
    private String authorizationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authorization_id", insertable = false, updatable = false)
    private AuthorizationEntity authorization;

    @Column(name = "role_id", nullable = false, updatable = false)
    private String roleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    private RoleEntity role;

    public AuthorizationRoleDto toDto() {
        return AuthorizationRoleDto.builder()
                .id(getId())
                .authorizationId(authorizationId)
                .authorization(authorization)
                .roleId(roleId)
                .role(role)
                .createdAt(getCreatedAt())
                .createMemberId(getCreateMemberId())
                .createdBy(getCreatedBy())
                .updatedAt(getUpdatedAt())
                .updateMemberId(getUpdateMemberId())
                .updatedBy(getUpdatedBy())
                .build();
    }
}
