package com.footbolic.api.common_code.entity;

import com.footbolic.api.common.entity.ExtendedBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@Entity(name = "CommonCodeEntity")
@Table(name = "CommonCode")
public class CommonCodeEntity extends ExtendedBaseEntity {

    @Column(name = "title", nullable = false, length = 20)
    private String title;

    @Column(name = "code", nullable = false, length = 20)
    private String code;

}
