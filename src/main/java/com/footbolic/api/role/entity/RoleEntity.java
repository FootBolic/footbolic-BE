package com.footbolic.api.role.entity;

import com.footbolic.api.authorization.entity.AuthorizationEntity;
import com.footbolic.api.common.entity.ExtendedBaseEntity;
import com.footbolic.api.member.entity.MemberEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@SuperBuilder
@Entity(name = "RoleEntity")
@Table(name = "Role")
public class RoleEntity extends ExtendedBaseEntity {

    @Column(name = "title", nullable = false, length = 20)
    private String title;

    @Builder.Default
    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private List<MemberEntity> members = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private List<AuthorizationEntity> authorizations = new ArrayList<>();

}
