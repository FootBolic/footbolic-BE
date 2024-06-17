package com.footbolic.api.comment.service;

import com.footbolic.api.comment.dto.CommentDto;

import java.util.List;

public interface CommentService {

    List<CommentDto> findAll(String postId);

    long count(String postId);

    CommentDto findById(String id);

    CommentDto saveComment(CommentDto role);

    void deleteComment(String id);

    boolean existsById(String id);

}