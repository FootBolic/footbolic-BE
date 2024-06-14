package com.footbolic.api.recommendation.repository;

import com.footbolic.api.recommendation.entity.CommentRecommendationEntity;
import com.footbolic.api.recommendation.entity.PostRecommendationEntity;
import com.footbolic.api.recommendation.entity.ReplyRecommendationEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.footbolic.api.recommendation.entity.QCommentRecommendationEntity.commentRecommendationEntity;
import static com.footbolic.api.recommendation.entity.QPostRecommendationEntity.postRecommendationEntity;
import static com.footbolic.api.recommendation.entity.QReplyRecommendationEntity.replyRecommendationEntity;


@Repository
@RequiredArgsConstructor
public class RecommendationRepositoryCustomImpl implements RecommendationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CommentRecommendationEntity> findAllCommentRecommendations(String commentId) {
        return queryFactory.selectFrom(commentRecommendationEntity)
                .where(commentRecommendationEntity.commentId.eq(commentId))
                .orderBy(commentRecommendationEntity.id.asc())
                .fetch();
    }

    @Override
    public List<PostRecommendationEntity> findAllPostRecommendations(String postId) {
        return queryFactory.selectFrom(postRecommendationEntity)
                .where(postRecommendationEntity.postId.eq(postId))
                .orderBy(postRecommendationEntity.id.asc())
                .fetch();
    }

    @Override
    public List<ReplyRecommendationEntity> findAllReplyRecommendations(String replyId) {
        return queryFactory.selectFrom(replyRecommendationEntity)
                .where(replyRecommendationEntity.replyId.eq(replyId))
                .orderBy(replyRecommendationEntity.id.asc())
                .fetch();
    }

    @Override
    public long countCommentRecommendations(String commentId) {
        return queryFactory.selectFrom(commentRecommendationEntity)
                .where(commentRecommendationEntity.commentId.eq(commentId))
                .orderBy(commentRecommendationEntity.id.asc())
                .fetch()
                .size();
    }

    @Override
    public long countPostRecommendations(String postId) {
        return queryFactory.selectFrom(postRecommendationEntity)
                .where(postRecommendationEntity.postId.eq(postId))
                .orderBy(postRecommendationEntity.id.asc())
                .fetch()
                .size();
    }

    @Override
    public long countReplyRecommendations(String replyId) {
        return queryFactory.selectFrom(replyRecommendationEntity)
                .where(replyRecommendationEntity.replyId.eq(replyId))
                .orderBy(replyRecommendationEntity.id.asc())
                .fetch()
                .size();
    }

    @Override
    public void deleteCommentRecommendation(String memberId, String commentId) {
        queryFactory.delete(commentRecommendationEntity)
                .where(
                        commentRecommendationEntity.memberId.eq(memberId)
                                .and(commentRecommendationEntity.commentId.eq(commentId))
                )
                .execute();
    }

    @Override
    public void deletePostRecommendation(String memberId, String postId) {
        queryFactory.delete(postRecommendationEntity)
                .where(
                        postRecommendationEntity.memberId.eq(memberId)
                                .and(postRecommendationEntity.postId.eq(postId))
                )
                .execute();
    }

    @Override
    public void deleteReplyRecommendation(String memberId, String replyId) {
        queryFactory.delete(replyRecommendationEntity)
                .where(
                        replyRecommendationEntity.memberId.eq(memberId)
                                .and(replyRecommendationEntity.replyId.eq(replyId))
                )
                .execute();
    }

    @Override
    public boolean commentRecommendationExists(String memberId, String commentId) {
        return !queryFactory.selectFrom(commentRecommendationEntity)
                .where(
                        commentRecommendationEntity.memberId.eq(memberId)
                                .and(
                                        commentRecommendationEntity.commentId.eq(commentId)
                                )
                ).fetch().isEmpty();
    }

    @Override
    public boolean postRecommendationExists(String memberId, String postId) {
        return !queryFactory.selectFrom(postRecommendationEntity)
                .where(
                        postRecommendationEntity.memberId.eq(memberId)
                                .and(
                                        postRecommendationEntity.postId.eq(postId)
                                )
                ).fetch().isEmpty();
    }

    @Override
    public boolean replyRecommendationExists(String memberId, String replyId) {
        return !queryFactory.selectFrom(replyRecommendationEntity)
                .where(
                        replyRecommendationEntity.memberId.eq(memberId)
                                .and(
                                        replyRecommendationEntity.replyId.eq(replyId)
                                )
                ).fetch().isEmpty();
    }
}
