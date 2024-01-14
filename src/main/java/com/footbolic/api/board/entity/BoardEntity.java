package com.footbolic.api.board.entity;

import com.footbolic.api.common.entity.ExtendedBaseEntity;
import com.footbolic.api.post.entity.PostEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Getter
@SuperBuilder
@Entity(name = "BoardEntity")
@Table(name = "Board")
public class BoardEntity extends ExtendedBaseEntity {

    @Column(name = "title", nullable = false, length = 20)
    private String title;

    @ColumnDefault("true")
    @Column(name = "is_secretable", columnDefinition = "TINYINT(1)", nullable = false)
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

    @Builder.Default
    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY)
    private List<PostEntity> posts = new ArrayList<>();

}
