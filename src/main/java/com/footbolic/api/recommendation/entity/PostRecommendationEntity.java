package com.footbolic.api.recommendation.entity;

import com.footbolic.api.post.entity.PostEntity;
import com.footbolic.api.recommendation.dto.PostRecommendationDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@Entity(name = "PostRecommendationEntity")
@Table(name = "PostRecommendation")
public class PostRecommendationEntity extends RecommendationEntity {

    @Column(name = "post_id", nullable = false, updatable = false, length = 30)
    private String postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", insertable = false, updatable = false)
    private PostEntity post;

    @Override
    public PostRecommendationDto toDto() {
        return PostRecommendationDto.builder()
                .id(getId())
                .memberId(getMemberId())
                .postId(postId)
                .createdAt(getCreatedAt())
                .updatedAt(getUpdatedAt())
                .build();
    }
}
