package com.footbolic.api.recommendation.entity;

import com.footbolic.api.comment.entity.CommentEntity;
import com.footbolic.api.recommendation.dto.CommentRecommendationDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@Entity(name = "CommentRecommendationEntity")
@Table(name = "CommentRecommendation")
public class CommentRecommendationEntity extends RecommendationEntity {

    @Column(name = "comment_id", nullable = false, updatable = false, length = 30)
    private String commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", insertable = false, updatable = false)
    private CommentEntity comment;

    @Override
    public CommentRecommendationDto toDto() {
        return CommentRecommendationDto.builder()
                .id(getId())
                .memberId(getMemberId())
                .commentId(commentId)
                .createdAt(getCreatedAt())
                .updatedAt(getUpdatedAt())
                .build();
    }
}
