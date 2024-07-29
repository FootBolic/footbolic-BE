package com.footbolic.api.board.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.footbolic.api.board.entity.BoardEntity;
import com.footbolic.api.member.dto.MemberDto;
import com.footbolic.api.menu.dto.MenuDto;
import com.footbolic.api.post.dto.PostDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Schema(name = "게시판 DTO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto {

    private String id;

    private String title;

    @JsonProperty("isSecretable")
    private boolean isSecretable;

    @JsonProperty("isRecommendable")
    private boolean isRecommendable;

    @JsonProperty("isCommentable")
    private boolean isCommentable;

    @JsonProperty("isAnnounceable")
    private boolean isAnnounceable;

    @JsonProperty("isUsed")
    private boolean isUsed;

    @Builder.Default
    private List<PostDto> posts = new ArrayList<>();

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

    private MenuDto menu;

    public BoardEntity toEntity() {
        return BoardEntity.builder()
                .id(id)
                .title(title)
                .isSecretable(isSecretable)
                .isRecommendable(isRecommendable)
                .isCommentable(isCommentable)
                .isAnnounceable(isAnnounceable)
                .isUsed(isUsed)
                .createdAt(createdAt)
                .createMemberId(createMemberId)
                .createdBy(createdBy == null ? null : createdBy.toEntity())
                .updatedAt(updatedAt)
                .updateMemberId(updateMemberId)
                .updatedBy(updatedBy == null ? null : updatedBy.toEntity())
                .build();
    }

}
