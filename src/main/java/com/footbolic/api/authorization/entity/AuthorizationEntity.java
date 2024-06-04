package com.footbolic.api.authorization.entity;

import com.footbolic.api.authorization.dto.AuthorizationDto;
import com.footbolic.api.common.entity.ExtendedBaseEntity;
import com.footbolic.api.authorization_role.entity.AuthorizationRoleEntity;
import com.footbolic.api.menu.entity.MenuEntity;
import com.footbolic.api.role.entity.RoleEntity;
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
@Entity(name = "AuthorizationEntity")
@Table(name = "Authorization")
public class AuthorizationEntity extends ExtendedBaseEntity {

    @Column(name = "title", nullable = false, length = 20)
    private String title;

    @Builder.Default
    @OneToMany(mappedBy = "authorization", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<AuthorizationRoleEntity> authorizationRoles = new ArrayList<>();

    @Builder.Default
    @Transient
    private List<RoleEntity> roles = new ArrayList<>();

    @Column(name = "menu_id", nullable = false, length = 30)
    private String menuId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", insertable = false, updatable = false)
    private MenuEntity menu;

    public AuthorizationDto toDto() {
        return AuthorizationDto.builder()
                .id(getId())
                .title(title)
                .menuId(menuId)
                .menu(menu == null ? null : menu.toDto())
                .createdAt(getCreatedAt())
                .createMemberId(getCreateMemberId())
                .createdBy(getCreatedBy() == null ? null : getCreatedBy().toDto())
                .updatedAt(getUpdatedAt())
                .updateMemberId(getUpdateMemberId())
                .updatedBy(getUpdatedBy() == null ? null : getUpdatedBy().toDto())
                .build();
    }

}
