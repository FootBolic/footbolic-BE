package com.footbolic.api.menu.entity;

import com.footbolic.api.common.entity.ExtendedBaseEntity;
import com.footbolic.api.menu.dto.MenuDto;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@SuperBuilder
@Entity(name = "MenuEntity")
@Table(name = "Menu")
public class MenuEntity extends ExtendedBaseEntity {

    @Column(name = "parent_id", length = 30)
    private String parentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", insertable = false, updatable = false)
    private MenuEntity parent;

    @Builder.Default
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<MenuEntity> menus = new ArrayList<>();

    @Column(name = "title", nullable = false, length = 20)
    private String title;

    @Column(name = "path", length = 100)
    private String path;

    @Column(name = "icon_code_id", length = 30)
    private String iconCodeId;

    public MenuDto toDto() {
        return MenuDto.builder()
                .id(getId())
                .parentId(parentId)
                .parent(parent)
                .menus(menus)
                .title(title)
                .path(path)
                .iconCodeId(iconCodeId)
                .createdAt(getCreatedAt())
                .createMemberId(getCreateMemberId())
                .createdBy(getCreatedBy())
                .updatedAt(getUpdatedAt())
                .updateMemberId(getUpdateMemberId())
                .updatedBy(getUpdatedBy())
                .build();
    }

}
