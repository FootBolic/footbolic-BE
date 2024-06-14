package com.footbolic.api.comment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.footbolic.api.comment.entity.CommentEntity;
import com.footbolic.api.member.dto.MemberDto;
import com.footbolic.api.post.dto.PostDto;
import com.footbolic.api.recommendation.dto.CommentRecommendationDto;
import com.footbolic.api.reply.dto.ReplyDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Schema(name = "댓글 DTO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private String id;

    private String postId;

    private PostDto post;

    private String content;

    @Builder.Default
    private List<ReplyDto> replies = new ArrayList<>();

    @Builder.Default
    private List<CommentRecommendationDto> recommendations = new ArrayList<>();

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

    public CommentEntity toEntity() {
        return CommentEntity.builder()
                .id(id)
                .content(content)
                .postId(postId)
                .createdAt(createdAt)
                .createMemberId(createMemberId)
                .createdBy(createdBy == null ? null : createdBy.toEntity())
                .updatedAt(updatedAt)
                .updateMemberId(updateMemberId)
                .updatedBy(updatedBy == null ? null : updatedBy.toEntity())
                .build();
    }
}
