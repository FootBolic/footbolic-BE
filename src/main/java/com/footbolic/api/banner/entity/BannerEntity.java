package com.footbolic.api.banner.entity;

import com.footbolic.api.common.entity.ExtendedBaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
@Entity(name = "BannerEntity")
@Table(name = "Banner")
public class BannerEntity extends ExtendedBaseEntity {

    @Column(name = "image_path", nullable = false, length = 100)
    private String imagePath;

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

}
