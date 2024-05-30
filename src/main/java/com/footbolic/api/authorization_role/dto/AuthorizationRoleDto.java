package com.footbolic.api.authorization_role.dto;

import com.footbolic.api.authorization.entity.AuthorizationEntity;
import com.footbolic.api.authorization_role.entity.AuthorizationRoleEntity;
import com.footbolic.api.member.entity.MemberEntity;
import com.footbolic.api.role.entity.RoleEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(name = "역할 - 권한 매핑 DTO")
@Data
@Builder
public class AuthorizationRoleDto {

    private String id;

    private String authorizationId;

    private AuthorizationEntity authorization;

    private String roleId;

    private RoleEntity role;

    private LocalDateTime createdAt;

    private String createMemberId;

    private MemberEntity createdBy;

    private LocalDateTime updatedAt;

    private String updateMemberId;

    private MemberEntity updatedBy;

    public AuthorizationRoleEntity toEntity() {
        return AuthorizationRoleEntity.builder()
                .id(id)
                .authorizationId(authorizationId)
                .authorization(authorization)
                .roleId(roleId)
                .role(role)
                .createdAt(createdAt)
                .createMemberId(createMemberId)
                .createdBy(createdBy)
                .updatedAt(updatedAt)
                .updateMemberId(updateMemberId)
                .updatedBy(updatedBy)
                .build();
    }
}