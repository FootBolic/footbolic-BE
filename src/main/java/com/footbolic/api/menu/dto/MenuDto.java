package com.footbolic.api.menu.dto;

import com.footbolic.api.member.entity.MemberEntity;
import com.footbolic.api.menu.entity.MenuEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Schema(name = "Menu 객체")
@Data
@Builder
@Setter
@Getter
public class MenuDto {

    private String id;

    private String parentId;

    private MenuEntity parent;

    @Builder.Default
    private List<MenuEntity> menus = new ArrayList<>();

    private String title;

    private String path;

    private String iconCodeId;

    private LocalDateTime createdAt;

    private String createMemberId;

    private MemberEntity createdBy;

    private LocalDateTime updatedAt;

    private String updateMemberId;

    private MemberEntity updatedBy;

    public MenuEntity toEntity() {
        return MenuEntity.builder()
                .id(id)
                .parentId(parentId)
                .parent(parent)
                .menus(menus)
                .title(title)
                .path(path)
                .iconCodeId(iconCodeId)
                .createdAt(createdAt)
                .createMemberId(createMemberId)
                .createdBy(createdBy)
                .updatedAt(updatedAt)
                .updateMemberId(updateMemberId)
                .updatedBy(updatedBy)
                .build();
    }

}
