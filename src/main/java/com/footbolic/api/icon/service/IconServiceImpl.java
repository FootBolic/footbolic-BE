package com.footbolic.api.icon.service;

import com.footbolic.api.icon.dto.IconDto;
import com.footbolic.api.icon.entity.IconEntity;
import com.footbolic.api.icon.repository.IconRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class IconServiceImpl implements IconService {

    private final IconRepository iconRepository;

    @Override
    public List<IconDto> findAll(Pageable pageable, String searchTitle, String searchCode) {
        return iconRepository.findAll(pageable, searchTitle, searchCode)
                .stream()
                .map(IconEntity::toDto)
                .toList();
    }

    @Override
    public long count(String searchTitle, String searchCode) {
        return iconRepository.count(searchTitle, searchCode);
    }

    @Override
    public IconDto findById(String id) {
        return iconRepository.findById(id).map(IconEntity::toDto).orElse(null);
    }

    @Override
    public IconDto save(IconDto icon) {
        return iconRepository.save(icon.toEntity()).toDto();
    }

    @Override
    public void delete(String id) {
        iconRepository.deleteById(id);
    }

    @Override
    public boolean existsById(String id) {
        return iconRepository.existsById(id);
    }

}
