package com.footbolic.api.reply.entity;

import com.footbolic.api.comment.entity.CommentEntity;
import com.footbolic.api.common.entity.ExtendedBaseEntity;
import com.footbolic.api.reply.dto.ReplyDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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

    public ReplyDto toDto() {
        return ReplyDto.builder()
                .id(getId())
                .commentId(commentId)
                .content(content)
                .createdAt(getCreatedAt())
                .createMemberId(getCreateMemberId())
                .createdBy(getCreatedBy() == null ? null : getCreatedBy().toDto())
                .updatedAt(getUpdatedAt())
                .updateMemberId(getUpdateMemberId())
                .updatedBy(getUpdatedBy() == null ? null : getUpdatedBy().toDto())
                .build();
    }
}
