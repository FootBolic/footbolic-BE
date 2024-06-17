package com.footbolic.api.reply.service;

import com.footbolic.api.reply.dto.ReplyDto;
import com.footbolic.api.reply.entity.ReplyEntity;
import com.footbolic.api.member.service.MemberService;
import com.footbolic.api.reply.repository.ReplyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReplyServiceImpl implements ReplyService {

    private final ReplyRepository replyRepository;

    private final MemberService memberService;

    @Override
    public List<ReplyDto> findAll(String commentId) {
        return replyRepository.findAll(commentId)
                .stream()
                .map(ReplyEntity::toDto)
                .peek(e -> e.setCreatedBy(memberService.findById(e.getCreateMemberId()).toPublicDto()))
                .toList();
    }

    @Override
    public long count(String commentId) {
        return replyRepository.count(commentId);
    }

    @Override
    public ReplyDto findById(String id) {
        return replyRepository.findById(id).map(ReplyEntity::toDto).orElse(null);
    }

    @Override
    public ReplyDto saveReply(ReplyDto reply) {
        return replyRepository.save(reply.toEntity()).toDto();
    }

    @Override
    public void deleteReply(String id) {
        replyRepository.deleteById(id);
    }

    @Override
    public boolean existsById(String id) {
        return replyRepository.existsById(id);
    }

}
