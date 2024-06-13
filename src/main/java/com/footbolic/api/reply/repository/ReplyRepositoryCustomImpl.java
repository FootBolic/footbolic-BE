package com.footbolic.api.reply.repository;

import com.footbolic.api.reply.entity.ReplyEntity;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.footbolic.api.reply.entity.QReplyEntity.replyEntity;


@Repository
@RequiredArgsConstructor
public class ReplyRepositoryCustomImpl implements ReplyRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ReplyEntity> findAll(String commentId) {

        JPAQuery<ReplyEntity> query = queryFactory.selectFrom(replyEntity);

        if (commentId != null && !commentId.isBlank()) {
            query.where(replyEntity.commentId.eq(commentId));
        }

        return query.orderBy(replyEntity.id.asc()).fetch();
    }

    @Override
    public long count(String commentId) {
        JPAQuery<ReplyEntity> query = queryFactory.selectFrom(replyEntity);

        if (commentId != null && !commentId.isBlank()) {
            query.where(replyEntity.commentId.eq(commentId));
        }

        return query.fetch().size();
    }
}
