package com.footbolic.api.recommendation.entity;

import com.footbolic.api.recommendation.dto.ReplyRecommendationDto;
import com.footbolic.api.reply.entity.ReplyEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@Entity(name = "ReplyRecommendationEntity")
@Table(name = "ReplyRecommendation")
public class ReplyRecommendationEntity extends RecommendationEntity {

    @Column(name = "reply_id", nullable = false, updatable = false, length = 30)
    private String replyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_id", insertable = false, updatable = false)
    private ReplyEntity reply;

    @Override
    public ReplyRecommendationDto toDto() {
        return ReplyRecommendationDto.builder()
                .id(getId())
                .memberId(getMemberId())
                .member(getMember() == null ? null : getMember().toDto())
                .replyId(replyId)
                .reply(reply == null ? null : reply.toDto())
                .createdAt(getCreatedAt())
                .updatedAt(getUpdatedAt())
                .build();
    }

}
