package com.footbolic.api.board.controller;

import com.footbolic.api.annotation.RoleCheck;
import com.footbolic.api.annotation.RoleCode;
import com.footbolic.api.board.dto.BoardDto;
import com.footbolic.api.board.service.BoardService;
import com.footbolic.api.common.entity.BaseResponse;
import com.footbolic.api.common.entity.ErrorResponse;
import com.footbolic.api.common.entity.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "게시판 API")
@RequestMapping("/boards")
@RequiredArgsConstructor
@RestController
@Slf4j
public class BoardController {

    private final BoardService boardService;

    @Operation(summary = "게시판 목록 조회", description = "게시판 목록을 page 단위로 조회")
    @ResponseStatus(HttpStatus.OK)
    @RoleCheck(codes = {
            @RoleCode(code = RoleCode.ROLE_BOARD_MNG)
    })
    @GetMapping
    public SuccessResponse getBoardList(
            Pageable pageable,
            @RequestParam(name = "searchTitle", required = false) String searchTitle
    ) {
        Map<String, Object> result = new HashMap<>();
        result.put("boards", boardService.findAll(pageable, searchTitle));
        result.put("size", boardService.count(searchTitle));

        return new SuccessResponse(result);
    }

    @Operation(summary = "전체 게시판 목록 조회", description = "전체 게시판 목록을 조회한다.")
    @ResponseStatus(HttpStatus.OK)
    @RoleCheck(codes = {
            @RoleCode(code = RoleCode.ROLE_BOARD_MNG)
    })
    @GetMapping("/all")
    public SuccessResponse getAllBoardList() {
        Map<String, Object> result = new HashMap<>();
        result.put("boards", boardService.findAll());

        return new SuccessResponse(result);
    }

    @Operation(summary = "게시판 생성", description = "파라미터로 전달 받은 게시판을 생성한다.")
    @Parameter(name = "board", description = "생성할 게시판 객체", required = true)
    @RoleCheck(codes = {
            @RoleCode(code = RoleCode.ROLE_BOARD_MNG)
    })
    @PostMapping
    public ResponseEntity<BaseResponse> createBoard(
            @RequestBody @Valid BoardDto board
    ) {
        BoardDto createdBoard = boardService.saveBoard(board);

        Map<String, Object> result = new HashMap<>();
        result.put("createdBoard", createdBoard);

        return ResponseEntity.ok(new SuccessResponse(result));
    }

    @Operation(summary = "게시판 단건 조회", description = "전달 받은 식별번호를 가진 게시판을 조회한다.")
    @Parameter(name = "id", description = "게시판 식별번호", required = true)
    @RoleCheck(codes = {
            @RoleCode(code = RoleCode.ROLE_BOARD_MNG)
    })
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getBoard(
            @PathVariable(name = "id") String id
    ) {
        BoardDto board = boardService.findById(id);

        if (board != null) {

            Map<String, Object> result = new HashMap<>();
            result.put("board", board);

            return ResponseEntity.ok(new SuccessResponse(result));
        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse("조회된 게시판이 없습니다."));
        }
    }

    @Operation(summary = "게시판 수정", description = "파라미터로 전달 받은 게시판을 수정한다.")
    @Parameter(name = "board", description = "수정할 게시판 객체", required = true)
    @RoleCheck(codes = {
            @RoleCode(code = RoleCode.ROLE_BOARD_MNG)
    })
    @PatchMapping
    public ResponseEntity<BaseResponse> updateBoard(
            @RequestBody @Valid BoardDto board
    ) {
        if (board.getId() == null || board.getId().isBlank()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("유효하지 않은 게시판 정보입니다."));
        } else if (boardService.existsById(board.getId())) {
            BoardDto updatedBoard = boardService.saveBoard(board);

            Map<String, Object> result = new HashMap<>();
            result.put("updatedBoard", updatedBoard);

            return ResponseEntity.ok(new SuccessResponse(result));
        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse("조회된 게시판이 없습니다."));
        }
    }

    @Operation(summary = "게시판 삭제", description = "제공된 식별번호를 가진 게시판을 삭제한다.")
    @Parameter(name = "board", description = "수정할 게시판 객체", required = true)
    @RoleCheck(codes = {
            @RoleCode(code = RoleCode.ROLE_BOARD_MNG)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteBoard(
            @PathVariable(name = "id") String id
    ) {
        if (id == null || id.isBlank()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("유효하지 않은 게시판 식별번호입니다."));
        } else if (boardService.existsById(id)) {
            boardService.deleteBoard(id);

            Map<String, String> result = new HashMap<>();
            result.put("id", id);

            return ResponseEntity.ok(new SuccessResponse(result));
        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse("조회된 게시판이 없습니다."));
        }
    }
}
