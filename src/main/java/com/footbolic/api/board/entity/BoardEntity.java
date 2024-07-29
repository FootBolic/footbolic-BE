package com.footbolic.api.board.entity;

import com.footbolic.api.board.dto.BoardDto;
import com.footbolic.api.common.entity.ExtendedBaseEntity;
import com.footbolic.api.post.entity.PostEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Schema(name = "게시판 Entity")
@Getter
@SuperBuilder
@NoArgsConstructor
@Entity(name = "BoardEntity")
@Table(name = "Board")
public class BoardEntity extends ExtendedBaseEntity {

    @Column(name = "title", nullable = false, length = 20)
    private String title;

    @ColumnDefault("true")
    @Column(name = "is_secret able", columnDefinition = "TINYINT(1)", nullable = false)
    private boolean isSecretable;

    @ColumnDefault("true")
    @Column(name = "is_recommendable", columnDefinition = "TINYINT(1)", nullable = false)
    private boolean isRecommendable;

    @ColumnDefault("true")
    @Column(name = "is_commentable", columnDefinition = "TINYINT(1)", nullable = false)
    private boolean isCommentable;

    @ColumnDefault("true")
    @Column(name = "is_announceable", columnDefinition = "TINYINT(1)", nullable = false)
    private boolean isAnnounceable;

    @ColumnDefault("true")
    @Column(name = "is_used", columnDefinition = "TINYINT(1)", nullable = false)
    private boolean isUsed;

    @ColumnDefault("false")
    @Column(name = "is_main", columnDefinition = "TINYINT(1)", nullable = false)
    private boolean isMain;

    @Builder.Default
    @Transient
    private List<PostEntity> posts = new ArrayList<>();

    public BoardDto toDto() {
        return BoardDto.builder()
                .id(getId())
                .title(title)
                .isSecretable(isSecretable)
                .isRecommendable(isRecommendable)
                .isCommentable(isCommentable)
                .isAnnounceable(isAnnounceable)
                .isUsed(isUsed)
                .posts(posts == null ? null : posts.stream().map(PostEntity::toDto).toList())
                .createdAt(getCreatedAt())
                .createMemberId(getCreateMemberId())
                .createdBy(getCreatedBy() == null ? null : getCreatedBy().toDto())
                .updatedAt(getUpdatedAt())
                .updateMemberId(getUpdateMemberId())
                .updatedBy(getUpdatedBy() == null ? null : getUpdatedBy().toDto())
                .build();
    }

}
