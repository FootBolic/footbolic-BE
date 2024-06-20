package com.footbolic.api.banner.service;

import com.footbolic.api.banner.dto.BannerDto;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface BannerService {

    List<BannerDto> findAll(Pageable pageable, String searchTitle, LocalDateTime searchDate);

    long count(String searchTitle, LocalDateTime searchDate);

    BannerDto findById(String id);

    BannerDto save(BannerDto role);

    void deleteById(String id);

    boolean existsById(String id);

}