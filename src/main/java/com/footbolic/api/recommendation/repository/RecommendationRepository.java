package com.footbolic.api.recommendation.repository;

import com.footbolic.api.recommendation.entity.RecommendationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendationRepository extends JpaRepository<RecommendationEntity, String>, RecommendationRepositoryCustom {
}