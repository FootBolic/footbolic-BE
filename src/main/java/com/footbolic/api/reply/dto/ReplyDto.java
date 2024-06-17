package com.footbolic.api.reply.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.footbolic.api.comment.dto.CommentDto;
import com.footbolic.api.member.dto.MemberDto;
import com.footbolic.api.recommendation.dto.ReplyRecommendationDto;
import com.footbolic.api.reply.entity.ReplyEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Schema(name = "답글 DTO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReplyDto {

    private String id;

    private String commentId;

    private CommentDto comment;

    private String content;

    @Builder.Default
    private List<ReplyRecommendationDto> recommendations = new ArrayList<>();

    private long recommendationsSize;

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

    @JsonProperty("isEditable")
    private boolean isEditable;

    @JsonProperty("isRecommended")
    private boolean isRecommended;

    public ReplyEntity toEntity() {
        return ReplyEntity.builder()
                .id(id)
                .commentId(commentId)
                .content(content)
                .createdAt(createdAt)
                .createMemberId(createMemberId)
                .createdBy(createdBy == null ? null : createdBy.toEntity())
                .updatedAt(updatedAt)
                .updateMemberId(updateMemberId)
                .updatedBy(updatedBy == null ? null : updatedBy.toEntity())
                .build();
    }
}
