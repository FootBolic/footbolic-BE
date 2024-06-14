package com.footbolic.api.recommendation.repository;

import com.footbolic.api.recommendation.entity.CommentRecommendationEntity;
import com.footbolic.api.recommendation.entity.PostRecommendationEntity;
import com.footbolic.api.recommendation.entity.ReplyRecommendationEntity;

import java.util.List;

public interface RecommendationRepositoryCustom {

    List<CommentRecommendationEntity> findAllCommentRecommendations(String commentId);

    List<PostRecommendationEntity> findAllPostRecommendations(String postId);

    List<ReplyRecommendationEntity> findAllReplyRecommendations(String replyId);

    long countCommentRecommendations(String commentId);

    long countPostRecommendations(String postId);

    long countReplyRecommendations(String replyId);

    void deleteCommentRecommendation(String memberId, String commentId);

    void deletePostRecommendation(String memberId, String postId);

    void deleteReplyRecommendation(String memberId, String replyId);

    boolean commentRecommendationExists(String memberId, String commentId);

    boolean postRecommendationExists(String memberId, String postId);

    boolean replyRecommendationExists(String memberId, String replyId);

}
