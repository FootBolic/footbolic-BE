package com.footbolic.api.authorization.entity;

import com.footbolic.api.common.entity.ExtendedBaseEntity;
import com.footbolic.api.member.entity.MemberEntity;
import com.footbolic.api.role.entity.RoleEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@Entity(name = "AuthorizationEntity")
@Table(name = "Authorization")
public class AuthorizationEntity extends ExtendedBaseEntity {

    @Column(name = "title", nullable = false, length = 20)
    private String title;

    @Column(name = "role_id", nullable = false, length = 30)
    private String roleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    private RoleEntity role;

    @Column(name = "menu_id", nullable = false, length = 30)
    private String menuId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", insertable = false, updatable = false)
    private MemberEntity menu;

}
