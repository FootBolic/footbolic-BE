package com.footbolic.api.role.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.footbolic.api.authorization.dto.AuthorizationDto;
import com.footbolic.api.member.dto.MemberDto;
import com.footbolic.api.member.entity.MemberEntity;
import com.footbolic.api.role.entity.RoleEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Schema(name = "역할 DTO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {

    private String id;

    private String title;

    private String code;

    @JsonProperty("isDefault")
    private boolean isDefault;

    @Builder.Default
    private List<MemberDto> members = new ArrayList<>();

    @Builder.Default
    private List<AuthorizationDto> authorizations = new ArrayList<>();

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

    public RoleEntity toEntity() {
        return RoleEntity.builder()
                .id(id)
                .title(title)
                .code(code)
                .isDefault(isDefault)
                .members(members == null ? null : members.stream().map(MemberDto::toEntity).toList())
                .authorizations(authorizations == null ? null : authorizations.stream().map(AuthorizationDto::toEntity).toList())
                .createdAt(createdAt)
                .createMemberId(createMemberId)
                .createdBy(createdBy == null ? null : createdBy.toEntity())
                .updatedAt(updatedAt)
                .updateMemberId(updateMemberId)
                .updatedBy(updatedBy == null ? null : updatedBy.toEntity())
                .build();
    }

}
