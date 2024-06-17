package com.footbolic.api.recommendation.repository;

import com.footbolic.api.recommendation.entity.ReplyRecommendationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyRecommendationRepository extends JpaRepository<ReplyRecommendationEntity, String> {
}