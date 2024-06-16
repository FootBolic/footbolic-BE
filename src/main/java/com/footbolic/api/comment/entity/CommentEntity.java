package com.footbolic.api.comment.entity;

import com.footbolic.api.comment.dto.CommentDto;
import com.footbolic.api.common.entity.ExtendedBaseEntity;
import com.footbolic.api.post.entity.PostEntity;
import com.footbolic.api.recommendation.entity.CommentRecommendationEntity;
import com.footbolic.api.reply.entity.ReplyEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

@Getter
@SuperBuilder
@NoArgsConstructor
@Entity(name = "CommentEntity")
@Table(name = "Comment")
public class CommentEntity extends ExtendedBaseEntity {

    @Column(name = "post_id", nullable = false, length = 30)
    private String postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", insertable = false, updatable = false)
    private PostEntity post;

    @Column(name = "content", nullable = false, length = 400)
    private String content;

    @Builder.Default
    @OneToMany(mappedBy = "comment", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @OrderBy("id ASC")
    private List<ReplyEntity> replies = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "comment", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<CommentRecommendationEntity> recommendations = new ArrayList<>();

    public CommentDto toDto() {
        boolean isRecommended = false;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && recommendations != null) {
            for (CommentRecommendationEntity recommendation : recommendations) {
                if (recommendation.getMemberId().equals(authentication.getCredentials().toString())) {
                    isRecommended = true;
                    break;
                }
            }
        }

        return CommentDto.builder()
                .id(getId())
                .postId(postId)
                .content(content)
                .replies(replies == null ? null : replies.stream().map(ReplyEntity::toDto).toList())
                .recommendationsSize(recommendations == null ? 0 : recommendations.size())
                .isRecommended(isRecommended)
                .createdAt(getCreatedAt())
                .createMemberId(getCreateMemberId())
                .createdBy(getCreatedBy() == null ? null : getCreatedBy().toDto())
                .updatedAt(getUpdatedAt())
                .updateMemberId(getUpdateMemberId())
                .updatedBy(getUpdatedBy() == null ? null : getUpdatedBy().toDto())
                .build();
    }

}
