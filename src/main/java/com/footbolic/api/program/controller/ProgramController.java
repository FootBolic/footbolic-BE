package com.footbolic.api.program.controller;

import com.footbolic.api.common.entity.BaseResponse;
import com.footbolic.api.common.entity.ErrorResponse;
import com.footbolic.api.common.entity.SuccessResponse;
import com.footbolic.api.program.dto.ProgramDto;
import com.footbolic.api.program.service.ProgramService;
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

@Tag(name = "프로그램 API")
@RequestMapping("/programs")
@RequiredArgsConstructor
@RestController
@Slf4j
public class ProgramController {

    private final ProgramService programService;

    @Operation(summary = "프로그램 목록 조회", description = "프로그램 목록을 page 단위로 조회")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public SuccessResponse getProgramList(
            Pageable pageable,
            @RequestParam(name = "searchTitle", required = false) String searchTitle,
            @RequestParam(name = "searchMenuId", required = false) String searchMenuId
    ) {
        Map<String, Object> result = new HashMap<>();
        result.put("programs", programService.findAll(pageable, searchTitle, searchMenuId));
        result.put("size", programService.count(searchTitle, searchMenuId));

        return new SuccessResponse(result);
    }

    @Operation(summary = "전체 프로그램 목록 조회", description = "전체 프로그램 목록을 조회한다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/all")
    public SuccessResponse getAllProgramList() {
        Map<String, Object> result = new HashMap<>();
        result.put("programs", programService.findAll());

        return new SuccessResponse(result);
    }

    @Operation(summary = "프로그램 생성", description = "파라미터로 전달 받은 프로그램을 생성")
    @Parameter(name = "program", description = "생성할 프로그램 객체", required = true)
    @PostMapping
    public ResponseEntity<BaseResponse> createProgram(
            @RequestBody @Valid ProgramDto program
    ) {
        ProgramDto createdProgram = programService.save(program);
        return ResponseEntity.ok(new SuccessResponse(createdProgram));
    }

    @Operation(summary = "프로그램 단건 조회", description = "전달 받은 식별번호를 가진 프로그램 조회")
    @Parameter(name = "id", description = "프로그램 식별번호", required = true)
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getProgram(
            @PathVariable(name = "id") String id
    ) {
        ProgramDto program = programService.findById(id);

        if (program != null) {
            return ResponseEntity.ok(new SuccessResponse(program));
        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse("조회된 프로그램이 없습니다."));
        }
    }

    @Operation(summary = "프로그램 수정", description = "파라미터로 전달 받은 프로그램을 수정")
    @Parameter(name = "program", description = "수정할 프로그램 객체", required = true)
    @PatchMapping
    public ResponseEntity<BaseResponse> updateProgram(
            @RequestBody @Valid ProgramDto program
    ) {
        if (program.getId() == null || program.getId().isBlank()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("유효하지 않은 프로그램정보입니다."));
        } else if (programService.existsById(program.getId())) {
            ProgramDto updatedProgram = programService.save(program);
            return ResponseEntity.ok(new SuccessResponse(updatedProgram));
        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse("조회된 프로그램이 없습니다."));
        }
    }

    @Operation(summary = "프로그램 삭제", description = "제공된 식별번호를 가진 프로그램 삭제")
    @Parameter(name = "program", description = "수정할 프로그램 객체", required = true)
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteProgram(
            @PathVariable(name = "id") String id
    ) {
        if (id == null || id.isBlank()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("유효하지 않은 프로그램 식별번호입니다"));
        } else if (programService.existsById(id)) {
            programService.deleteById(id);

            Map<String, String> result = new HashMap<>();
            result.put("id", id);

            return ResponseEntity.ok(new SuccessResponse(result));
        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse("조회된 프로그램이 없습니다."));
        }
    }
}
