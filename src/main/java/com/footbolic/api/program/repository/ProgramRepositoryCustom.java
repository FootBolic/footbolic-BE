package com.footbolic.api.program.repository;

import com.footbolic.api.program.entity.ProgramEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProgramRepositoryCustom {

    List<ProgramEntity> findAll(Pageable pageable, String searchTitle, String searchMenuId);

    long count(String searchTitle, String searchMenuId);

}
