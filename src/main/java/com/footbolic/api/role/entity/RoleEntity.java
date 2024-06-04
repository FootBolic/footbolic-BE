package com.footbolic.api.role.entity;

import com.footbolic.api.authorization.entity.AuthorizationEntity;
import com.footbolic.api.common.entity.ExtendedBaseEntity;
import com.footbolic.api.authorization_role.entity.AuthorizationRoleEntity;
import com.footbolic.api.member.entity.MemberEntity;
import com.footbolic.api.member_role.entity.MemberRoleEntity;
import com.footbolic.api.role.dto.RoleDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.List;

@Getter
@SuperBuilder
@NoArgsConstructor
@Entity(name = "RoleEntity")
@Table(name = "Role")
public class RoleEntity extends ExtendedBaseEntity implements GrantedAuthority {

    @Column(name = "title", nullable = false, length = 20)
    private String title;

    @Column(name = "code", nullable = false, length = 20, unique = true)
    private String code;

    @ColumnDefault("false")
    @Column(name = "is_default", columnDefinition = "TINYINT(1)", nullable = false)
    private boolean isDefault;

    @Builder.Default
    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<MemberRoleEntity> memberRoles = new ArrayList<>();

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
                .code(code)
                .isDefault(isDefault)
                .authorizations(authorizationRoles.stream().map(AuthorizationRoleEntity::getAuthorization).map(AuthorizationEntity::toDto).toList())
                .createdAt(getCreatedAt())
                .createMemberId(getCreateMemberId())
                .createdBy(getCreatedBy() == null ? null : getCreatedBy().toDto())
                .updatedAt(getUpdatedAt())
                .updateMemberId(getUpdateMemberId())
                .updatedBy(getUpdatedBy() == null ? null : getUpdatedBy().toDto())
                .build();
    }

    @Override
    public String getAuthority() {
        return this.code;
    }
}
