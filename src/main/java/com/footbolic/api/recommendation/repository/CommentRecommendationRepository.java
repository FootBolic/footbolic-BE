package com.footbolic.api.recommendation.repository;

import com.footbolic.api.recommendation.entity.CommentRecommendationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRecommendationRepository extends JpaRepository<CommentRecommendationEntity, String> {
}