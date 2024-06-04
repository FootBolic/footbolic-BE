package com.footbolic.api.program.entity;

import com.footbolic.api.common.entity.ExtendedBaseEntity;
import com.footbolic.api.menu.entity.MenuEntity;
import com.footbolic.api.program.dto.ProgramDto;
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
@Entity(name = "ProgramEntity")
@Table(name = "Program")
public class ProgramEntity extends ExtendedBaseEntity {

    @Column(name = "title", nullable = false, length = 20)
    private String title;

    @Column(name = "code", nullable = false, unique = true, length = 20)
    private String code;

    @Column(name = "path", length = 100)
    private String path;

    @Builder.Default
    @Transient
    private List<MenuEntity> menus = new ArrayList<>();

    public ProgramDto toDto() {
        return ProgramDto.builder()
                .id(getId())
                .title(title)
                .code(code)
                .path(path)
                .menus(menus == null ? null : menus.stream().map(MenuEntity::toDto).toList())
                .createdAt(getCreatedAt())
                .createMemberId(getCreateMemberId())
                .createdBy(getCreatedBy() == null ? null : getCreatedBy().toDto())
                .updatedAt(getUpdatedAt())
                .updateMemberId(getUpdateMemberId())
                .updatedBy(getUpdatedBy() == null ? null : getUpdatedBy().toDto())
                .build();
    }

}
