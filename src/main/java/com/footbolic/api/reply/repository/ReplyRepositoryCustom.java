package com.footbolic.api.reply.repository;

import com.footbolic.api.reply.entity.ReplyEntity;

import java.util.List;

public interface ReplyRepositoryCustom {

    List<ReplyEntity> findAll(String commentId);

    long count(String commentId);

}
