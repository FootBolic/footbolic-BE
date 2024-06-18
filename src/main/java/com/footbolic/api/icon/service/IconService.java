package com.footbolic.api.icon.service;

import com.footbolic.api.icon.dto.IconDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IconService {

    List<IconDto> findAll(Pageable pageable, String searchTitle, String searchCode);

    long count(String searchTitle, String searchCode);

    IconDto findById(String id);

    IconDto save(IconDto role);

    void delete(String id);

    boolean existsById(String id);

}