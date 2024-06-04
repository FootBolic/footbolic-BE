package com.footbolic.api.menu.entity;

import com.footbolic.api.common.entity.ExtendedBaseEntity;
import com.footbolic.api.menu.dto.MenuDto;
import com.footbolic.api.program.entity.ProgramEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "parent_id", insertable = false, updatable = false)
    @OrderBy(" order ASC, id DESC ")
    private List<MenuEntity> children = new ArrayList<>();

    @Column(name = "title", nullable = false, length = 20)
    private String title;

    @Column(name = "program_id", length = 30)
    private String programId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id", insertable = false, updatable = false)
    private ProgramEntity program;

    @Column(name = "icon_code_id", length = 30)
    private String iconCodeId;

    @ColumnDefault("true")
    @Column(name = "is_used", columnDefinition = "TINYINT(1)", nullable = false)
    private boolean isUsed;

    @ColumnDefault("0")
    @Column(name = "order_no", nullable = false)
    private long order;

    public MenuDto toDto() {
        return MenuDto.builder()
                .id(getId())
                .parentId(parentId)
                .children(children == null ? null : children.stream().map(MenuEntity::toDto).toList())
                .title(title)
                .programId(programId)
                .program(program == null ? null : program.toDto())
                .iconCodeId(iconCodeId)
                .isUsed(isUsed)
                .order(order)
                .createdAt(getCreatedAt())
                .createMemberId(getCreateMemberId())
                .createdBy(getCreatedBy() == null ? null : getCreatedBy().toDto())
                .updatedAt(getUpdatedAt())
                .updateMemberId(getUpdateMemberId())
                .updatedBy(getUpdatedBy() == null ? null : getUpdatedBy().toDto())
                .build();
    }

}
