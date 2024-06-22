package com.footbolic.api.banner.repository;

import com.footbolic.api.banner.entity.BannerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BannerRepository extends JpaRepository<BannerEntity, String>, BannerRepositoryCustom {
}
