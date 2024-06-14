package com.footbolic.api.recommendation.repository;

import com.footbolic.api.recommendation.entity.PostRecommendationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRecommendationRepository extends JpaRepository<PostRecommendationEntity, String> {
}