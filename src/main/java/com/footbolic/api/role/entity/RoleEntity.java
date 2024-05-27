package com.footbolic.api.role.entity;

import com.footbolic.api.authorization.entity.AuthorizationEntity;
import com.footbolic.api.common.entity.ExtendedBaseEntity;
import com.footbolic.api.common.entity.map.AuthorizationRoleEntity;
import com.footbolic.api.member.entity.MemberEntity;
import com.footbolic.api.role.dto.RoleDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

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

    @ColumnDefault("false")
    @Column(name = "is_default", columnDefinition = "TINYINT(1)", nullable = false)
    private boolean isDefault;

    @Builder.Default
    @Transient
    private List<MemberEntity> members = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<AuthorizationRoleEntity> authorizationRoles = new ArrayList<>();

    @Builder.Default
    @Transient
    private List<AuthorizationEntity> authorizations = new ArrayList<>();

    public RoleDto toDto() {
        return RoleDto.builder()
                .id(getId())
                .title(title)
                .isDefault(isDefault)
                .members(members.stream().map(MemberEntity::toDto).toList())
                .authorizations(authorizationRoles.stream().map(AuthorizationRoleEntity::getAuthorization).map(AuthorizationEntity::toDto).toList())
                .createdAt(getCreatedAt())
                .createMemberId(getCreateMemberId())
                .createdBy(getCreatedBy())
                .updatedAt(getUpdatedAt())
                .updateMemberId(getUpdateMemberId())
                .updatedBy(getUpdatedBy())
                .build();
    }

}
