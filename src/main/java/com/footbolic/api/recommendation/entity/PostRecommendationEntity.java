package com.footbolic.api.recommendation.entity;

import com.footbolic.api.common.entity.BaseEntity;
import com.footbolic.api.post.entity.PostEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@Entity(name = "PostRecommendationEntity")
@Table(name = "PostRecommendation")
public class PostRecommendationEntity extends BaseEntity {

    @Column(name = "member_id", nullable = false, updatable = false, length = 30)
    private String memberId;

    @Column(name = "post_id", nullable = false, updatable = false, length = 30)
    private String postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", insertable = false, updatable = false)
    private PostEntity post;

}
