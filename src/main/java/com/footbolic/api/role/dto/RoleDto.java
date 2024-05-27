package com.footbolic.api.role.dto;

import com.footbolic.api.authorization.dto.AuthorizationDto;
import com.footbolic.api.member.dto.MemberDto;
import com.footbolic.api.member.entity.MemberEntity;
import com.footbolic.api.role.entity.RoleEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Schema(name = "Role 객체")
@Data
@Builder
public class RoleDto {

    private String id;

    private String title;

    private boolean isDefault;

    @Builder.Default
    private List<MemberDto> members = new ArrayList<>();

    @Builder.Default
    private List<AuthorizationDto> authorizations = new ArrayList<>();

    private LocalDateTime createdAt;

    private String createMemberId;

    private MemberEntity createdBy;

    private LocalDateTime updatedAt;

    private String updateMemberId;

    private MemberEntity updatedBy;

    public RoleEntity toEntity() {
        return RoleEntity.builder()
                .id(id)
                .title(title)
                .isDefault(isDefault)
                .authorizations(authorizations.stream().map(AuthorizationDto::toEntity).toList())
                .createdAt(createdAt)
                .createMemberId(createMemberId)
                .createdBy(createdBy)
                .updatedAt(updatedAt)
                .updateMemberId(updateMemberId)
                .updatedBy(updatedBy)
                .build();
    }

}
