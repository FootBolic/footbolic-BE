package com.footbolic.api.board.repository;

import com.footbolic.api.board.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, String>, BoardRepositoryCustom {
}
