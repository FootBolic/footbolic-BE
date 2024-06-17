package com.footbolic.api.file.entity;

import com.footbolic.api.common.entity.ExtendedBaseEntity;
import com.footbolic.api.file.dto.FileDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@Entity(name = "FileEntity")
@Table(name = "File")
public class FileEntity extends ExtendedBaseEntity {

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

    @Column(name = "extension", nullable = false, length = 20)
    private String extension;

    public FileDto toDto() {
        return FileDto.builder()
                .id(getId())
                .originalName(originalName)
                .newName(newName)
                .type(type)
                .size(size)
                .path(path)
                .extension(extension)
                .createdAt(getCreatedAt())
                .createMemberId(getCreateMemberId())
                .createdBy(getCreatedBy() == null ? null : getCreatedBy().toDto())
                .updatedAt(getUpdatedAt())
                .updateMemberId(getUpdateMemberId())
                .updatedBy(getUpdatedBy() == null ? null : getUpdatedBy().toDto())
                .build();
    }
}
