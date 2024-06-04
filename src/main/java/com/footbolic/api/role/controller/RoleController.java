package com.footbolic.api.role.controller;

import com.footbolic.api.authorization_role.dto.AuthorizationRoleDto;
import com.footbolic.api.authorization_role.service.AuthorizationRoleService;
import com.footbolic.api.common.entity.BaseResponse;
import com.footbolic.api.common.entity.ErrorResponse;
import com.footbolic.api.common.entity.SuccessResponse;
import com.footbolic.api.role.dto.RoleDto;
import com.footbolic.api.role.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "역할 API")
@RequestMapping("/roles")
@RequiredArgsConstructor
@RestController
@Slf4j
public class RoleController {

    private final RoleService roleService;

    private final AuthorizationRoleService authorizationRoleService;

    @Operation(summary = "역할 목록 조회", description = "역할 목록을 page 단위로 조회")
    @Parameter(name = "searchTitle", description = "제목 검색 파라미터")
    @Parameter(name = "searchAuthorizationId", description = "권한 식별번호 검색 파라미터")
    @Parameter(name = "size", description = "결과 목록 크기")
    @Parameter(name = "page", description = "결과 목록 페이지")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public SuccessResponse getRoleList(
            Pageable pageable,
            @RequestParam(name = "searchTitle", required = false) String searchTitle,
            @RequestParam(name = "searchAuthorizationId", required = false) String searchAuthorizationId
    ) {
        Map<String, Object> result = new HashMap<>();
        result.put("roles", roleService.findAll(pageable, searchTitle, searchAuthorizationId));
        result.put("size", roleService.count(searchTitle, searchAuthorizationId));
        return new SuccessResponse(result);
    }

    @Operation(summary = "역할 목록 조회", description = "역할 목록을 page 단위로 조회한다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/all")
    public SuccessResponse getAllRoleList() {
        Map<String, Object> result = new HashMap<>();
        result.put("roles", roleService.findAll());

        return new SuccessResponse(result);
    }

    @Operation(summary = "역할 생성", description = "파라미터로 전달 받은 역할를 생성한다.")
    @Parameter(name = "role", description = "생성할 역할 객체", required = true)
    @PostMapping
    public ResponseEntity<BaseResponse> createRole(
            @RequestBody @Valid RoleDto role
    ) {
        RoleDto createdRole = roleService.saveRole(role);

        role.getAuthorizations().stream().filter(e -> !e.isDeleted())
                .forEach(auth -> {
                    AuthorizationRoleDto authRole = AuthorizationRoleDto.builder()
                            .roleId(createdRole.getId())
                            .authorizationId(auth.getId())
                            .build();
                    authorizationRoleService.saveAuthorizationRole(authRole);
                });

        RoleDto createdRoleAndAuth = roleService.findById(createdRole.getId());

        Map<String, Object> result = new HashMap<>();
        result.put("createdRole", createdRoleAndAuth);

        return ResponseEntity.ok(new SuccessResponse(result));
    }

    @Operation(summary = "역할 단건 조회", description = "전달 받은 result 가진 역할 조회한다.")
    @Parameter(name = "id", description = "역할 식별번호", required = true)
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getRole(
            @PathVariable(name = "id") String id
    ) {
        RoleDto role = roleService.findById(id);

        if (role != null) {
            Map<String, Object> result = new HashMap<>();
            result.put("role", role);

            return ResponseEntity.ok(new SuccessResponse(result));
        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse("조회된 역할이 없습니다."));
        }
    }

    @Operation(summary = "역할 수정", description = "파라미터로 전달 받은 역할를 수정한다.")
    @Parameter(name = "role", description = "수정할 역할 객체", required = true)
    @PatchMapping
    public ResponseEntity<BaseResponse> updateRole(
            @RequestBody @Valid RoleDto role
    ) {
        if (role.getId() == null || role.getId().isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("유효하지 않은 역할정보입니다."));
        } else if (roleService.existsById(role.getId())) {
            role.getAuthorizations().forEach(auth -> {
                if (auth.isNew() && !auth.isDeleted()) {
                    authorizationRoleService.saveAuthorizationRole(AuthorizationRoleDto.builder()
                            .roleId(role.getId())
                            .authorizationId(auth.getId())
                            .build());
                } else if (!auth.isNew() && auth.isDeleted()) {
                    authorizationRoleService.deleteByAuthorizationIdAndRoleId(auth.getId(), role.getId());
                }
            });

            RoleDto updatedRole = roleService.saveRole(role);

            Map<String, Object> result = new HashMap<>();
            result.put("updatedRole", updatedRole);

            return ResponseEntity.ok(new SuccessResponse(result));
        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse("조회된 역할이 없습니다."));
        }
    }

    @Operation(summary = "역할 삭제", description = "제공된 식별번호를 가진 역할 삭제한다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteRole(
            @PathVariable(name = "id") String id
    ) {
        if (id == null || id.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("유효하지 않은 역할정보입니다."));
        } else if (roleService.existsById(id)) {
            roleService.deleteRole(id);

            Map<String, Object> result = new HashMap<>();
            result.put("id", id);

            return ResponseEntity.ok(new SuccessResponse(result));
        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse("조회된 역할이 없습니다."));
        }
    }
}
