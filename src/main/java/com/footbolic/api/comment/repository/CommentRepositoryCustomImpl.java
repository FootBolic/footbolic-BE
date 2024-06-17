package com.footbolic.api.comment.repository;

import com.footbolic.api.comment.entity.CommentEntity;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.footbolic.api.comment.entity.QCommentEntity.commentEntity;


@Repository
@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CommentEntity> findAll(String postId) {

        JPAQuery<CommentEntity> query = queryFactory.selectFrom(commentEntity);

        if (postId != null && !postId.isBlank()) {
            query.where(commentEntity.postId.eq(postId));
        }

        return query.orderBy(commentEntity.id.asc()).fetch();
    }

    @Override
    public long count(String postId) {
        JPAQuery<CommentEntity> query = queryFactory.selectFrom(commentEntity);

        if (postId != null && !postId.isBlank()) {
            query.where(commentEntity.postId.eq(postId));
        }

        return query.fetch().size();
    }
}
