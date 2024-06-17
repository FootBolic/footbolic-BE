package com.footbolic.api.post.repository;

import com.footbolic.api.post.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, String>, PostRepositoryCustom {
}
