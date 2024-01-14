package com.footbolic.api.recommendation.entity;

import com.footbolic.api.common.entity.BaseEntity;
import com.footbolic.api.member.entity.MemberEntity;
import com.footbolic.api.reply.entity.ReplyEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@Entity(name = "ReplyRecommendationEntity")
@Table(name = "ReplyRecommendation")
public class ReplyRecommendationEntity extends BaseEntity {

    @Column(name = "member_id", nullable = false, updatable = false, length = 30)
    private String memberId;

    @Column(name = "reply_id", nullable = false, updatable = false, length = 30)
    private String replyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_id", insertable = false, updatable = false)
    private ReplyEntity reply;

}
