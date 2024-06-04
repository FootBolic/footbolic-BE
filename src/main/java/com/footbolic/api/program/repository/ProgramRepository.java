package com.footbolic.api.program.repository;

import com.footbolic.api.program.entity.ProgramEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramRepository extends JpaRepository<ProgramEntity, String>, ProgramRepositoryCustom {
}
