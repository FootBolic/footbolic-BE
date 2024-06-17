package com.footbolic.api.comment.repository;

import com.footbolic.api.comment.entity.CommentEntity;

import java.util.List;

public interface CommentRepositoryCustom {

    List<CommentEntity> findAll(String postId);

    long count(String postId);

}
