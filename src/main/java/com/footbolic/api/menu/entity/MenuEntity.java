package com.footbolic.api.menu.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.footbolic.api.common.entity.ExtendedBaseEntity;
import com.footbolic.api.menu.dto.MenuDto;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

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
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "parent_id", insertable = false, updatable = false)
    private List<MenuEntity> children = new ArrayList<>();

    @Column(name = "title", nullable = false, length = 20)
    private String title;

    @Column(name = "path", length = 100)
    private String path;

    @Column(name = "icon_code_id", length = 30)
    private String iconCodeId;

    @ColumnDefault("true")
    @Column(name = "is_used", columnDefinition = "TINYINT(1)", nullable = false)
    private boolean isUsed;

    public MenuDto toDto() {
        return MenuDto.builder()
                .id(getId())
                .parentId(parentId)
                .children(children == null ? null : children.stream().map(MenuEntity::toDto).toList())
                .title(title)
                .path(path)
                .iconCodeId(iconCodeId)
                .isUsed(isUsed)
                .createdAt(getCreatedAt())
                .createMemberId(getCreateMemberId())
                .createdBy(getCreatedBy())
                .updatedAt(getUpdatedAt())
                .updateMemberId(getUpdateMemberId())
                .updatedBy(getUpdatedBy())
                .build();
    }

}
