package com.footbolic.api.board.service;

import com.footbolic.api.board.entity.BoardEntity;
import com.footbolic.api.board.dto.BoardDto;
import com.footbolic.api.board.repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    @Override
    public List<BoardDto> findAll() {
        return boardRepository.findAll().stream().map(BoardEntity::toDto).toList();
    }

    @Override
    public List<BoardDto> findAll(Pageable pageable, String searchTitle) {
        return boardRepository.findAll(pageable, searchTitle)
                .stream()
                .map(BoardEntity::toDto)
                .toList();
    }

    @Override
    public long count(String searchTitle) {
        return boardRepository.count(searchTitle);
    }

    @Override
    public BoardDto findById(String id) {
        return boardRepository.findById(id).map(BoardEntity::toDto).orElse(null);
    }

    @Override
    public BoardDto saveBoard(BoardDto role) {
        BoardEntity createdBoard = boardRepository.save(role.toEntity());
        return createdBoard.toDto();
    }

    @Override
    public void deleteBoard(String id) {
        boardRepository.deleteById(id);
    }

    @Override
    public boolean existsById(String id) {
        return boardRepository.existsById(id);
    }

}
