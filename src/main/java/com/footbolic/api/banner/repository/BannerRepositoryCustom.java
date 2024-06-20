package com.footbolic.api.banner.repository;

import com.footbolic.api.banner.entity.BannerEntity;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface BannerRepositoryCustom {

    List<BannerEntity> findAll(Pageable pageable, String searchTitle, LocalDateTime searchDate);

    long count(String searchTitle, LocalDateTime searchDate);

}
