package com.footbolic.api.post.service;

import com.footbolic.api.post.dto.PostDto;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface PostService {

    List<PostDto> findAll(String boardId, Pageable pageable, String searchTitle, String searchCreatedBy, LocalDateTime searchCreatedAt);

    long count(String boardId, String searchTitle, String searchCreatedBy, LocalDateTime searchCreatedAt);

    PostDto findById(String id);

    PostDto savePost(PostDto role);

    void deletePost(String id);

    boolean existsById(String id);

}