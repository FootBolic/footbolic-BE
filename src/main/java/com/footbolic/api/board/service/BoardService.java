package com.footbolic.api.board.service;

import com.footbolic.api.board.dto.BoardDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardService {

    List<BoardDto> findAll();

    List<BoardDto> findAll(Pageable pageable, String searchTitle);

    long count(String searchTitle);

    BoardDto findById(String id);

    BoardDto saveBoard(BoardDto role);

    void deleteBoard(String id);

    boolean existsById(String id);

}