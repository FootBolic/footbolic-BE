package com.footbolic.api.post.repository;

import com.footbolic.api.post.entity.PostEntity;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepositoryCustom {

    List<PostEntity> findAll(String boardId, Pageable pageable, String searchTitle, String searchCreatedBy, LocalDateTime searchCreatedAt);

    long count(String boardId, String searchTitle, String searchCreatedBy, LocalDateTime searchCreatedAt);

}
