package com.footbolic.api.image.entity;

import com.footbolic.api.common.entity.ExtendedBaseEntity;
import com.footbolic.api.image.dto.ImageDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@Entity(name = "ImageEntity")
@Table(name = "Image")
public class ImageEntity extends ExtendedBaseEntity {

    @Column(name = "original_name", nullable = false, length = 100)
    private String originalName;

    @Column(name = "new_name", nullable = false, length = 100)
    private String newName;

    @Column(name = "type", nullable = false, length = 100)
    private String type;

    @Column(name = "size")
    private long size;

    @Column(name = "path", nullable = false, length = 100)
    private String path;

    public ImageDto toDto() {
        return ImageDto.builder()
                .id(getId())
                .originalName(originalName)
                .newName(newName)
                .type(type)
                .size(size)
                .path(path)
                .createdAt(getCreatedAt())
                .createMemberId(getCreateMemberId())
                .createdBy(getCreatedBy() == null ? null : getCreatedBy().toDto())
                .updatedAt(getUpdatedAt())
                .updateMemberId(getUpdateMemberId())
                .updatedBy(getUpdatedBy() == null ? null : getUpdatedBy().toDto())
                .build();
    }
}
