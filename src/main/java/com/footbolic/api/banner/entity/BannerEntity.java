package com.footbolic.api.banner.entity;

import com.footbolic.api.banner.dto.BannerDto;
import com.footbolic.api.common.entity.ExtendedBaseEntity;
import com.footbolic.api.file.entity.FileEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
@NoArgsConstructor
@Entity(name = "BannerEntity")
@Table(name = "Banner")
public class BannerEntity extends ExtendedBaseEntity {

    @Column(name = "title", nullable = false, length = 30)
    private String title;

    @Column(name = "file_id", nullable = false, length = 30)
    private String fileId;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "file_id", insertable = false, updatable = false)
    private FileEntity file;

    @ColumnDefault("false")
    @Column(name = "is_mobile", columnDefinition = "TINYINT(1)", nullable = false)
    private boolean isMobile;

    @ColumnDefault("false")
    @Column(name = "is_time_limited", columnDefinition = "TINYINT(1)", nullable = false)
    private boolean isTimeLimited;

    @Column(name = "starts_at", columnDefinition = "DATETIME")
    private LocalDateTime startsAt;

    @Column(name = "ends_at", columnDefinition = "DATETIME")
    private LocalDateTime endsAt;

    @ColumnDefault("false")
    @Column(name = "is_linked", columnDefinition = "TINYINT(1)", nullable = false)
    private boolean isLinked;

    @Column(name = "link_address", length = 100)
    private String linkAddress;

    public BannerDto toDto() {
        return BannerDto.builder()
                .id(getId())
                .title(title)
                .fileId(fileId)
                .file(file == null ? null : file.toDto())
                .isMobile(isMobile)
                .isLinked(isLinked)
                .startsAt(startsAt)
                .endsAt(endsAt)
                .isLinked(isLinked)
                .linkAddress(linkAddress)
                .createdAt(getCreatedAt())
                .createMemberId(getCreateMemberId())
                .createdBy(getCreatedBy() == null ? null : getCreatedBy().toDto())
                .updatedAt(getUpdatedAt())
                .updateMemberId(getUpdateMemberId())
                .updatedBy(getUpdatedBy() == null ? null : getUpdatedBy().toDto())
                .build();
    }

}
