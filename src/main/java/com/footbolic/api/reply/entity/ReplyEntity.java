package com.footbolic.api.reply.entity;

import com.footbolic.api.comment.entity.CommentEntity;
import com.footbolic.api.common.entity.ExtendedBaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
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

}
