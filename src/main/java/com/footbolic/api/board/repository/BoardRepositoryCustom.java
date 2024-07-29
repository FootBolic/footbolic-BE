package com.footbolic.api.board.repository;

import com.footbolic.api.board.entity.BoardEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardRepositoryCustom {

    List<BoardEntity> findAll(Pageable pageable, String searchTitle);

    long count(String searchTitle);

    List<BoardEntity> findMain();

}
