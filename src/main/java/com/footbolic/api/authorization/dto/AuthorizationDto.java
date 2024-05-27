package com.footbolic.api.authorization.dto;

import com.footbolic.api.authorization.entity.AuthorizationEntity;
import com.footbolic.api.member.entity.MemberEntity;
import com.footbolic.api.menu.dto.MenuDto;
import com.footbolic.api.role.dto.RoleDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Schema(name = "Authorization 객체")
@Data
@Builder
public class AuthorizationDto {

    private String id;

    private String title;

    @Builder.Default
    private List<RoleDto> roles = new ArrayList<>();

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
                .menuId(menuId)
                .menu(menu == null ? null : menu.toEntity())
                .roles(roles == null ? null : roles.stream().map(RoleDto::toEntity).toList())
                .createdAt(createdAt)
                .createMemberId(createMemberId)
                .createdBy(createdBy)
                .updatedAt(updatedAt)
                .updateMemberId(updateMemberId)
                .updatedBy(updatedBy)
                .build();
    }

}
