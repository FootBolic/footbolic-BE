package com.footbolic.api.authorization.dto;

import com.footbolic.api.authorization.entity.AuthorizationEntity;
import com.footbolic.api.member.entity.MemberEntity;
import com.footbolic.api.menu.dto.MenuDto;
import com.footbolic.api.role.dto.RoleDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(name = "Authorization 객체")
@Data
@Builder
public class AuthorizationDto {

    private String id;

    private String title;

    private String roleId;

    private RoleDto role;

    private String menuId;

    private MenuDto menu;

    private LocalDateTime createdAt;

    private String createMemberId;

    private MemberEntity createdBy;

    private LocalDateTime updatedAt;

    private String updateMemberId;

    private MemberEntity updatedBy;

    public AuthorizationEntity toEntity() {
        return AuthorizationEntity.builder()
                .id(id)
                .title(title)
                .roleId(roleId)
                .role(role == null ? null : role.toEntity())
                .menuId(menuId)
                .menu(menu == null ? null : menu.toEntity())
                .createdAt(createdAt)
                .createMemberId(createMemberId)
                .createdBy(createdBy)
                .updatedAt(updatedAt)
                .updateMemberId(updateMemberId)
                .updatedBy(updatedBy)
                .build();
    }

}
