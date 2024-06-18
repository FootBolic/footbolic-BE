package com.footbolic.api.icon.entity;

import com.footbolic.api.common.entity.ExtendedBaseEntity;
import com.footbolic.api.icon.dto.IconDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Schema(name = "아이콘 Entity")
@Getter
@SuperBuilder
@NoArgsConstructor
@Entity(name = "IconEntity")
@Table(name = "Icon")
public class IconEntity extends ExtendedBaseEntity {

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "code", nullable = false , length = 30)
    private String code;

    public IconDto toDto() {
        return IconDto.builder()
                .id(getId())
                .title(title)
                .code(code)
                .createdAt(getCreatedAt())
                .createMemberId(getCreateMemberId())
                .createdBy(getCreatedBy() == null ? null : getCreatedBy().toDto())
                .updatedAt(getUpdatedAt())
                .updateMemberId(getUpdateMemberId())
                .updatedBy(getUpdatedBy() == null ? null : getUpdatedBy().toDto())
                .build();
    }
}
