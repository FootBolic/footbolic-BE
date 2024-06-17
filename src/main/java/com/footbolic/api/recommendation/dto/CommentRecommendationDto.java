package com.footbolic.api.recommendation.dto;

import com.footbolic.api.comment.dto.CommentDto;
import com.footbolic.api.recommendation.entity.CommentRecommendationEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Schema(name = "댓글 추천 DTO")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CommentRecommendationDto extends RecommendationDto {

    private String commentId;

    private CommentDto comment;

    @Override
    public CommentRecommendationEntity toEntity() {
        return CommentRecommendationEntity.builder()
                .id(getId())
                .memberId(getMemberId())
                .commentId(commentId)
                .createdAt(getCreatedAt())
                .updatedAt(getUpdatedAt())
                .build();
    }
}
