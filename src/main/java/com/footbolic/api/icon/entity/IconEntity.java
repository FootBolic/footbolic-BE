package com.footbolic.api.icon.entity;

import com.footbolic.api.common.entity.ExtendedBaseEntity;
import com.footbolic.api.icon.dto.IconDto;
import com.footbolic.api.menu.entity.MenuEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Schema(name = "아이콘 Entity")
@Getter
@SuperBuilder
@NoArgsConstructor
@Entity(name = "IconEntity")
@Table(name = "Icon")
public class IconEntity extends ExtendedBaseEntity {

    @Column(name = "title", nullable = false, length = 20)
    private String title;

    @Column(name = "code", nullable = false , length = 30)
    private String code;

    @Column(name = "type", nullable = false , length = 20)
    private String type;

    @Transient
    @Builder.Default
    private List<MenuEntity> menus = new ArrayList<>();

    public IconDto toDto() {
        return IconDto.builder()
                .id(getId())
                .title(title)
                .code(code)
                .type(type)
                .createdAt(getCreatedAt())
                .createMemberId(getCreateMemberId())
                .createdBy(getCreatedBy() == null ? null : getCreatedBy().toDto())
                .updatedAt(getUpdatedAt())
                .updateMemberId(getUpdateMemberId())
                .updatedBy(getUpdatedBy() == null ? null : getUpdatedBy().toDto())
                .build();
    }
}
