package com.footbolic.api.comment.repository;

import com.footbolic.api.comment.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, String>, CommentRepositoryCustom {
}
