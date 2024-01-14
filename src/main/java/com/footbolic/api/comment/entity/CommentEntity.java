package com.footbolic.api.comment.entity;

import com.footbolic.api.common.entity.ExtendedBaseEntity;
import com.footbolic.api.post.entity.PostEntity;
import com.footbolic.api.reply.entity.ReplyEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@SuperBuilder
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
    @OneToMany(mappedBy = "comment", fetch = FetchType.LAZY)
    private List<ReplyEntity> replies = new ArrayList<>();

}
