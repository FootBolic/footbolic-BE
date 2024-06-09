package com.footbolic.api.post.entity;

import com.footbolic.api.board.entity.BoardEntity;
import com.footbolic.api.comment.entity.CommentEntity;
import com.footbolic.api.common.entity.ExtendedBaseEntity;
import com.footbolic.api.post.dto.PostDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Schema(name = "게시글 Entity")
@Getter
@SuperBuilder
@NoArgsConstructor
@Entity(name = "PostEntity")
@Table(name = "Post")
public class PostEntity extends ExtendedBaseEntity {

    @Column(name = "board_id", nullable = false, length = 30)
    private String boardId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", insertable = false, updatable = false)
    private BoardEntity board;

    @Column(name = "title", nullable = false, length = 20)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @ColumnDefault("false")
    @Column(name = "is_secret", columnDefinition = "TINYINT(1)", nullable = false)
    private boolean isSecret;

    @ColumnDefault("false")
    @Column(name = "is_announcement", columnDefinition = "TINYINT(1)", nullable = false)
    private boolean isAnnouncement;

    @Column(name = "announcement_starts_at")
    private LocalDateTime announcementStartsAt;

    @Column(name = "announcement_ends_at")
    private LocalDateTime announcementEndsAt;

    @Builder.Default
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<CommentEntity> comments = new ArrayList<>();

    public PostDto toDto() {
        return PostDto.builder()
                .id(getId())
                .title(title)
                .content(content)
                .boardId(boardId)
                .board(board == null ? null : board.toDto())
                .isSecret(isSecret)
                .isAnnouncement(isAnnouncement)
                .announcementStartsAt(announcementStartsAt)
                .announcementEndsAt(announcementEndsAt)
                .createdAt(getCreatedAt())
                .createMemberId(getCreateMemberId())
                .createdBy(getCreatedBy() == null ? null : getCreatedBy().toDto())
                .updatedAt(getUpdatedAt())
                .updateMemberId(getUpdateMemberId())
                .updatedBy(getUpdatedBy() == null ? null : getUpdatedBy().toDto())
                .build();
    }
}
