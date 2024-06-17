package com.footbolic.api.reply.entity;

import com.footbolic.api.comment.entity.CommentEntity;
import com.footbolic.api.common.entity.ExtendedBaseEntity;
import com.footbolic.api.recommendation.entity.ReplyRecommendationEntity;
import com.footbolic.api.reply.dto.ReplyDto;
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
@Entity(name = "ReplyEntity")
@Table(name = "Reply")
public class ReplyEntity extends ExtendedBaseEntity {

    @Column(name = "comment_id", nullable = false, length = 30)
    private String commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", insertable = false, updatable = false)
    private CommentEntity comment;

    @Column(name = "content", nullable = false, length = 400)
    private String content;

    @Builder.Default
    @OneToMany(mappedBy = "reply", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ReplyRecommendationEntity> recommendations = new ArrayList<>();

    public ReplyDto toDto() {
        boolean isRecommended = false;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && recommendations != null) {
            for (ReplyRecommendationEntity recommendation : recommendations) {
                if (recommendation.getMemberId().equals(authentication.getCredentials().toString())) {
                    isRecommended = true;
                    break;
                }
            }
        }

        return ReplyDto.builder()
                .id(getId())
                .commentId(commentId)
                .content(content)
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
