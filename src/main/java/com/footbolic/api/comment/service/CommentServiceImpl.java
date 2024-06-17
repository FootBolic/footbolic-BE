package com.footbolic.api.comment.service;

import com.footbolic.api.comment.dto.CommentDto;
import com.footbolic.api.comment.entity.CommentEntity;
import com.footbolic.api.comment.repository.CommentRepository;
import com.footbolic.api.member.service.MemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final MemberService memberService;

    @Override
    public List<CommentDto> findAll(String postId) {
        return commentRepository.findAll(postId)
                .stream()
                .map(CommentEntity::toDto)
                .peek(e -> e.setCreatedBy(memberService.findById(e.getCreateMemberId()).toPublicDto()))
                .toList();
    }

    @Override
    public long count(String postId) {
        return commentRepository.count(postId);
    }

    @Override
    public CommentDto findById(String id) {
        return commentRepository.findById(id).map(CommentEntity::toDto).orElse(null);
    }

    @Override
    public CommentDto saveComment(CommentDto comment) {
        return commentRepository.save(comment.toEntity()).toDto();
    }

    @Override
    public void deleteComment(String id) {
        commentRepository.deleteById(id);
    }

    @Override
    public boolean existsById(String id) {
        return commentRepository.existsById(id);
    }

}
