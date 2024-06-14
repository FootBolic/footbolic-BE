package com.footbolic.api.recommendation.dto;

import com.footbolic.api.post.dto.PostDto;
import com.footbolic.api.recommendation.entity.PostRecommendationEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Schema(name = "게시글 추천 DTO")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PostRecommendationDto extends RecommendationDto {
    private String postId;

    private PostDto post;

    @Override
    public PostRecommendationEntity toEntity() {
        return PostRecommendationEntity.builder()
                .id(getId())
                .memberId(getMemberId())
                .postId(postId)
                .createdAt(getCreatedAt())
                .updatedAt(getUpdatedAt())
                .build();
    }
}
