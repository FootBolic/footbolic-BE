package com.footbolic.api.authorization.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.footbolic.api.authorization.entity.AuthorizationEntity;
import com.footbolic.api.member.dto.MemberDto;
import com.footbolic.api.member.entity.MemberEntity;
import com.footbolic.api.menu.dto.MenuDto;
import com.footbolic.api.role.dto.RoleDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Schema(name = "Authorization 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorizationDto {

    private String id;

    private String title;

    @Builder.Default
    private List<RoleDto> roles = new ArrayList<>();

    private String menuId;

    private MenuDto menu;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdAt;

    private String createMemberId;

    private MemberDto createdBy;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updatedAt;

    private String updateMemberId;

    private MemberDto updatedBy;

    @JsonProperty("isNew")
    private boolean isNew;

    @JsonProperty("isDeleted")
    private boolean isDeleted;

    public AuthorizationEntity toEntity() {
        return AuthorizationEntity.builder()
                .id(id)
                .title(title)
                .menuId(menuId)
                .menu(menu == null ? null : menu.toEntity())
                .roles(roles == null ? null : roles.stream().map(RoleDto::toEntity).toList())
                .createdAt(createdAt)
                .createMemberId(createMemberId)
                .createdBy(createdBy == null ? null : createdBy.toEntity())
                .updatedAt(updatedAt)
                .updateMemberId(updateMemberId)
                .updatedBy(updatedBy == null ? null : updatedBy.toEntity())
                .build();
    }

}
