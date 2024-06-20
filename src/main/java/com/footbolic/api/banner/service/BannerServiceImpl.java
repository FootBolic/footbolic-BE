package com.footbolic.api.banner.service;

import com.footbolic.api.banner.dto.BannerDto;
import com.footbolic.api.banner.entity.BannerEntity;
import com.footbolic.api.banner.repository.BannerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BannerServiceImpl implements BannerService {

    private final BannerRepository bannerRepository;

    @Override
    public List<BannerDto> findAll(Pageable pageable, String searchTitle, LocalDateTime searchDate) {
        return bannerRepository.findAll(pageable, searchTitle, searchDate)
                .stream()
                .map(BannerEntity::toDto)
                .toList();
    }

    @Override
    public long count(String searchTitle, LocalDateTime searchDate) {
        return bannerRepository.count(searchTitle, searchDate);
    }

    @Override
    public BannerDto findById(String id) {
        return bannerRepository.findById(id).map(BannerEntity::toDto).orElse(null);
    }

    @Override
    public BannerDto save(BannerDto role) {
        BannerEntity createdBanner = bannerRepository.save(role.toEntity());
        return createdBanner.toDto();
    }

    @Override
    public void deleteById(String id) {
        try {
            bannerRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException(e.getMessage());
        }
    }

    @Override
    public boolean existsById(String id) {
        return bannerRepository.existsById(id);
    }

}
