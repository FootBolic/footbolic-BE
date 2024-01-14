package com.footbolic.api.recommendation.entity;

import com.footbolic.api.comment.entity.CommentEntity;
import com.footbolic.api.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@Entity(name = "CommentRecommendationEntity")
@Table(name = "CommentRecommendation")
public class CommentRecommendationEntity extends BaseEntity {

    @Column(name = "member_id", nullable = false, updatable = false, length = 30)
    private String memberId;

    @Column(name = "comment_id", nullable = false, updatable = false, length = 30)
    private String commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", insertable = false, updatable = false)
    private CommentEntity comment;

}
