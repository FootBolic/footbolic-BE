package com.footbolic.api.icon.repository;

import com.footbolic.api.icon.entity.IconEntity;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface IconRepositoryCustom {

    List<IconEntity> findAll(Pageable pageable, String searchTitle, String searchCode);

    long count(String searchTitle, String searchCode);

}
