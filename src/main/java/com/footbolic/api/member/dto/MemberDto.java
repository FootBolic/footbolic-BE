package com.footbolic.api.member.dto;

import com.footbolic.api.member.entity.MemberEntity;
import com.footbolic.api.role.dto.RoleDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(name = "Member 객체")
@Data
@Builder
public class MemberDto {

    private String id;

    private String title;

    private String roleId;

    private RoleDto role;

    private String fullName;

    private String nickname;

    private String refreshToken;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public MemberEntity toEntity() {
        return MemberEntity.builder()
                .id(id)
                .roleId(roleId)
                .role(role == null ? null : role.toEntity())
                .fullName(fullName)
                .nickname(nickname)
                .refreshToken(refreshToken)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

}
