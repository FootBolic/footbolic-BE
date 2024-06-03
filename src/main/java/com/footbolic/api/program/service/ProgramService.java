package com.footbolic.api.program.service;

import com.footbolic.api.program.dto.ProgramDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProgramService {

    List<ProgramDto> findAll();

    List<ProgramDto> findAll(Pageable pageable, String searchTitle, String searchMenuId);

    long count(String searchTitle, String searchMenuId);

    ProgramDto findById(String id);

    ProgramDto save(ProgramDto role);

    void deleteById(String id);

    boolean existsById(String id);

}