package com.footbolic.api.reply.service;

import com.footbolic.api.reply.dto.ReplyDto;

import java.util.List;

public interface ReplyService {

    List<ReplyDto> findAll(String commentId);

    long count(String commentId);

    ReplyDto findById(String id);

    ReplyDto saveReply(ReplyDto role);

    void deleteReply(String id);

    boolean existsById(String id);

}