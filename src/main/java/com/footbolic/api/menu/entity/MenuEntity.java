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
@NoArgsConstructor
@Entity(name = "MenuEntity")
@Table(name = "Menu")
public class MenuEntity extends ExtendedBaseEntity {

    @Column(name = "parent_id", length = 30)
    private String parentId;

    @Builder.Default
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id", insertable = false, updatable = false)
    private List<MenuEntity> children = new ArrayList<>();

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
                .children(children.stream().map(MenuEntity::toDto).toList())
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
