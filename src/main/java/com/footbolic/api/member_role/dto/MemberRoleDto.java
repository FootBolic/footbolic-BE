package com.footbolic.api.member_role.dto;

import com.footbolic.api.member.entity.MemberEntity;
import com.footbolic.api.member_role.entity.MemberRoleEntity;
import com.footbolic.api.role.entity.RoleEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(name = "회원 - 역할 매핑 DTO")
@Data
@Builder
public class MemberRoleDto {

    private String id;

    private String memberId;

    private MemberEntity member;

    private String roleId;

    private RoleEntity role;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public MemberRoleEntity toEntity() {
        return MemberRoleEntity.builder()
                .id(id)
                .memberId(memberId)
                .member(member)
                .roleId(roleId)
                .role(role)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }
}