package com.footbolic.api.recommendation.service;

import com.footbolic.api.recommendation.dto.CommentRecommendationDto;
import com.footbolic.api.recommendation.dto.PostRecommendationDto;
import com.footbolic.api.recommendation.dto.ReplyRecommendationDto;

import java.util.List;

public interface RecommendationService {

    List<CommentRecommendationDto> findAllCommentRecommendations(String commentId);

    List<PostRecommendationDto> findAllPostRecommendations(String postId);

    List<ReplyRecommendationDto> findAllReplyRecommendations(String replyId);

    long countCommentRecommendations(String commentId);

    long countPostRecommendations(String postId);

    long countReplyRecommendations(String replyId);

    CommentRecommendationDto saveCommentRecommendation(CommentRecommendationDto commentRecommendation);

    PostRecommendationDto savePostRecommendation(PostRecommendationDto postRecommendation);

    ReplyRecommendationDto saveReplyRecommendation(ReplyRecommendationDto replyRecommendation);

    void deleteCommentRecommendation(String memberId, String commentId);

    void deletePostRecommendation(String memberId, String postId);

    void deleteReplyRecommendation(String memberId, String replyId);

    boolean commentRecommendationExists(String memberId, String commentId);

    boolean postRecommendationExists(String memberId, String postId);

    boolean replyRecommendationExists(String memberId, String replyId);

}