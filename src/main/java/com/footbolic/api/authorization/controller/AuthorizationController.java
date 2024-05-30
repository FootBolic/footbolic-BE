package com.footbolic.api.authorization.controller;

import com.footbolic.api.common.entity.BaseResponse;
import com.footbolic.api.common.entity.ErrorResponse;
import com.footbolic.api.common.entity.SuccessResponse;
import com.footbolic.api.authorization.dto.AuthorizationDto;
import com.footbolic.api.authorization.service.AuthorizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "권한 API")
@RequestMapping("/authorizations")
@RequiredArgsConstructor
@RestController
@Slf4j
public class AuthorizationController {

    private final AuthorizationService authorizationService;

    @Operation(summary = "권한 목록 조회", description = "권한 목록을 page 단위로 조회")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public SuccessResponse getAuthorizationList(
            Pageable pageable,
            @RequestParam(name = "searchTitle", required = false) String searchTitle,
            @RequestParam(name = "searchMenuId", required = false) String searchMenuId
    ) {
        Map<String, Object> result = new HashMap<>();
        result.put("authorizations", authorizationService.findAll(pageable, searchTitle, searchMenuId));
        result.put("size", authorizationService.count(searchTitle, searchMenuId));

        return new SuccessResponse(result);
    }

    @Operation(summary = "권한 목록 조회", description = "권한 목록을 page 단위로 조회")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/all")
    public SuccessResponse getAllAuthorizationList() {
        Map<String, Object> result = new HashMap<>();
        result.put("authorizations", authorizationService.findAll());

        return new SuccessResponse(result);
    }

    @Operation(summary = "권한 생성", description = "파라미터로 전달 받은 권한를 생성")
    @Parameter(name = "authorization", description = "생성할 권한 객체", required = true)
    @PostMapping
    public ResponseEntity<BaseResponse> createAuthorization(
            @RequestBody @Valid AuthorizationDto authorization
    ) {
        AuthorizationDto createdAuthorization = authorizationService.saveAuthorization(authorization);
        return ResponseEntity.ok(new SuccessResponse(createdAuthorization));
    }

    @Operation(summary = "권한 단건 조회", description = "전달 받은 식별번호를 가진 권한 조회")
    @Parameter(name = "id", description = "권한 식별번호", required = true)
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getAuthorization(
            @PathVariable(name = "id") String id
    ) {
        AuthorizationDto authorization = authorizationService.findById(id);

        if (authorization != null) {
            return ResponseEntity.ok(new SuccessResponse(authorization));
        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse("조회된 권한이 없습니다."));
        }
    }

    @Operation(summary = "권한 수정", description = "파라미터로 전달 받은 권한를 수정")
    @Parameter(name = "authorization", description = "수정할 권한 객체", required = true)
    @PatchMapping
    public ResponseEntity<BaseResponse> updateAuthorization(
            @RequestBody @Valid AuthorizationDto authorization
    ) {
        if (authorization.getId() == null || authorization.getId().isBlank()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("유효하지 않은 권한정보입니다."));
        } else if (authorizationService.existsById(authorization.getId())) {
            AuthorizationDto updatedAuthorization = authorizationService.saveAuthorization(authorization);
            return ResponseEntity.ok(new SuccessResponse(updatedAuthorization));
        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse("조회된 권한이 없습니다."));
        }
    }

    @Operation(summary = "권한 삭제", description = "제공된 식별번호를 가진 권한 삭제")
    @Parameter(name = "authorization", description = "수정할 권한 객체", required = true)
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteAuthorization(
            @PathVariable(name = "id") String id
    ) {
        if (id == null || id.isBlank()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("유효하지 않은 권한 식별번호입니다"));
        } else if (authorizationService.existsById(id)) {
            authorizationService.deleteAuthorization(id);

            Map<String, String> result = new HashMap<>();
            result.put("id", id);

            return ResponseEntity.ok(new SuccessResponse(result));
        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse("조회된 권한이 없습니다."));
        }
    }
}
