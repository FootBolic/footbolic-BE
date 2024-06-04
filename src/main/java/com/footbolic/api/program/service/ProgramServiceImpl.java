package com.footbolic.api.program.service;

import com.footbolic.api.program.dto.ProgramDto;
import com.footbolic.api.program.entity.ProgramEntity;
import com.footbolic.api.program.repository.ProgramRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProgramServiceImpl implements ProgramService {

    private final ProgramRepository programRepository;

    @Override
    public List<ProgramDto> findAll() {
        return programRepository.findAll().stream().map(ProgramEntity::toDto).toList();
    }

    @Override
    public List<ProgramDto> findAll(Pageable pageable, String searchTitle, String searchMenuId) {
        return programRepository.findAll(pageable, searchTitle, searchMenuId)
                .stream()
                .map(ProgramEntity::toDto)
                .toList();
    }

    @Override
    public long count(String searchTitle, String searchMenuId) {
        return programRepository.count(searchTitle, searchMenuId);
    }

    @Override
    public ProgramDto findById(String id) {
        return programRepository.findById(id).map(ProgramEntity::toDto).orElse(null);
    }

    @Override
    public ProgramDto save(ProgramDto role) {
        ProgramEntity createdProgram = programRepository.save(role.toEntity());
        return createdProgram.toDto();
    }

    @Override
    public void deleteById(String id) {
        try {
            programRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException(e.getMessage());
        }
    }

    @Override
    public boolean existsById(String id) {
        return programRepository.existsById(id);
    }

}
