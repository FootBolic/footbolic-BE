package com.footbolic.api.recommendation.service;

import com.footbolic.api.recommendation.dto.CommentRecommendationDto;
import com.footbolic.api.recommendation.dto.PostRecommendationDto;
import com.footbolic.api.recommendation.dto.ReplyRecommendationDto;
import com.footbolic.api.recommendation.entity.CommentRecommendationEntity;
import com.footbolic.api.recommendation.entity.PostRecommendationEntity;
import com.footbolic.api.recommendation.entity.ReplyRecommendationEntity;
import com.footbolic.api.recommendation.repository.CommentRecommendationRepository;
import com.footbolic.api.recommendation.repository.PostRecommendationRepository;
import com.footbolic.api.recommendation.repository.RecommendationRepository;
import com.footbolic.api.recommendation.repository.ReplyRecommendationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RecommendationServiceImpl implements RecommendationService {

    private final RecommendationRepository recommendationRepository;

    private final CommentRecommendationRepository commentRecommendationRepository;

    private final PostRecommendationRepository postRecommendationRepository;

    private final ReplyRecommendationRepository replyRecommendationRepository;

    @Override
    public List<CommentRecommendationDto> findAllCommentRecommendations(String commentId) {
        return recommendationRepository.findAllCommentRecommendations(commentId)
                .stream()
                .map(CommentRecommendationEntity::toDto)
                .toList();
    }

    @Override
    public List<PostRecommendationDto> findAllPostRecommendations(String postId) {
        return recommendationRepository.findAllPostRecommendations(postId)
                .stream()
                .map(PostRecommendationEntity::toDto)
                .toList();
    }

    @Override
    public List<ReplyRecommendationDto> findAllReplyRecommendations(String replyId) {
        return recommendationRepository.findAllReplyRecommendations(replyId)
                .stream()
                .map(ReplyRecommendationEntity::toDto)
                .toList();
    }

    @Override
    public long countCommentRecommendations(String commentId) {
        return recommendationRepository.countCommentRecommendations(commentId);
    }

    @Override
    public long countPostRecommendations(String postId) {
        return recommendationRepository.countPostRecommendations(postId);
    }

    @Override
    public long countReplyRecommendations(String replyId) {
        return recommendationRepository.countReplyRecommendations(replyId);
    }

    @Override
    public CommentRecommendationDto saveCommentRecommendation(CommentRecommendationDto commentRecommendation) {
        return commentRecommendationRepository.save(commentRecommendation.toEntity()).toDto();
    }

    @Override
    public PostRecommendationDto savePostRecommendation(PostRecommendationDto postRecommendation) {
        return postRecommendationRepository.save(postRecommendation.toEntity()).toDto();
    }

    @Override
    public ReplyRecommendationDto saveReplyRecommendation(ReplyRecommendationDto replyRecommendation) {
        return replyRecommendationRepository.save(replyRecommendation.toEntity()).toDto();
    }

    @Override
    public void deleteCommentRecommendation(String memberId, String commentId) {
        recommendationRepository.deleteCommentRecommendation(memberId, commentId);
    }

    @Override
    public void deletePostRecommendation(String memberId, String postId) {
        recommendationRepository.deletePostRecommendation(memberId, postId);
    }

    @Override
    public void deleteReplyRecommendation(String memberId, String replyId) {
        recommendationRepository.deleteReplyRecommendation(memberId, replyId);
    }

    @Override
    public boolean commentRecommendationExists(String memberId, String commentId) {
        return recommendationRepository.commentRecommendationExists(memberId, commentId);
    }

    @Override
    public boolean postRecommendationExists(String memberId, String postId) {
        return recommendationRepository.postRecommendationExists(memberId, postId);
    }

    @Override
    public boolean replyRecommendationExists(String memberId, String replyId) {
        return recommendationRepository.replyRecommendationExists(memberId, replyId);
    }

}
