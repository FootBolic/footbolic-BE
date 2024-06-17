package com.footbolic.api.menu.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.footbolic.api.member.dto.MemberDto;
import com.footbolic.api.menu.entity.MenuEntity;
import com.footbolic.api.program.dto.ProgramDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Schema(name = "Menu 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuDto {

    private String id;

    private String parentId;

    private MenuDto parent;

    @Builder.Default
    private List<MenuDto> children = new ArrayList<>();

    private String title;

    private String programId;

    private ProgramDto program;

    private String detailId;

    private Object detail;

    private String iconCodeId;

    @JsonProperty("isUsed")
    private boolean isUsed;

    private long order;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdAt;

    private String createMemberId;

    private MemberDto createdBy;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updatedAt;

    private String updateMemberId;

    private MemberDto updatedBy;

    public MenuEntity toEntity() {
        return MenuEntity.builder()
                .id(id)
                .parentId(parentId)
                .children(children == null ? null : children.stream().map(MenuDto::toEntity).toList())
                .title(title)
                .programId(programId)
                .program(program == null ? null : program.toEntity())
                .detailId(detailId)
                .iconCodeId(iconCodeId)
                .isUsed(isUsed)
                .order(order)
                .createdAt(createdAt)
                .createMemberId(createMemberId)
                .createdBy(createdBy == null ? null : createdBy.toEntity())
                .updatedAt(updatedAt)
                .updateMemberId(updateMemberId)
                .updatedBy(updatedBy == null ? null : updatedBy.toEntity())
                .build();
    }

}
