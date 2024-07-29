package com.footbolic.api.board.service;

import com.footbolic.api.board.entity.BoardEntity;
import com.footbolic.api.board.dto.BoardDto;
import com.footbolic.api.board.repository.BoardRepository;
import com.footbolic.api.menu.dto.MenuDto;
import com.footbolic.api.menu.service.MenuService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    private final MenuService menuService;

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
        return boardRepository.save(role.toEntity()).toDto();
    }

    @Override
    public void deleteBoard(String id) {
        boardRepository.deleteById(id);
    }

    @Override
    public boolean existsById(String id) {
        return boardRepository.existsById(id);
    }

    @Override
    public List<BoardDto> findMain() {
        return boardRepository.findMain().stream()
                .map(board -> {
                    BoardDto dto = board.toDto();
                    MenuDto menu = menuService.findByBoardId(dto.getId());
                    if (menu == null) {
                        return null;
                    } else {
                        dto.setMenu(menu);
                        return dto;
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }

}
