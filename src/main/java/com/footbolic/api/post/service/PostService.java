package com.footbolic.api.post.service;

import com.footbolic.api.post.dto.PostDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {

    List<PostDto> findAll(String boardId, Pageable pageable, String searchTitle);

    long count(String boardId, String searchTitle);

    PostDto findById(String id);

    PostDto savePost(PostDto role);

    void deletePost(String id);

    boolean existsById(String id);

}