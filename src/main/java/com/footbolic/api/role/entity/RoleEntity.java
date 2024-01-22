package com.footbolic.api.role.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.footbolic.api.authorization.entity.AuthorizationEntity;
import com.footbolic.api.common.entity.ExtendedBaseEntity;
import com.footbolic.api.member.entity.MemberEntity;
import com.footbolic.api.role.dto.RoleDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@SuperBuilder
@NoArgsConstructor
@Entity(name = "RoleEntity")
@Table(name = "Role")
public class RoleEntity extends ExtendedBaseEntity {

    @Column(name = "title", nullable = false, length = 20)
    private String title;

    @JsonIgnore
    @Builder.Default
    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private List<MemberEntity> members = new ArrayList<>();

    @JsonIgnore
    @Builder.Default
    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private List<AuthorizationEntity> authorizations = new ArrayList<>();

    public RoleDto toDto() {System.out.println(">>>>>>>>>RoleEntity toDto");
        return RoleDto.builder()
                .id(getId())
                .title(title)
                .members(members == null ? null : members.stream().map(MemberEntity::toDto).toList())
                .authorizations(authorizations == null ? null : authorizations.stream().map(AuthorizationEntity::toDto).toList())
                .createdAt(getCreatedAt())
                .createMemberId(getCreateMemberId())
                .createdBy(getCreatedBy())
                .updatedAt(getUpdatedAt())
                .updateMemberId(getUpdateMemberId())
                .updatedBy(getUpdatedBy())
                .build();
    }

}
