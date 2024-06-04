package com.footbolic.api.menu.controller;

import com.footbolic.api.annotation.RoleCheck;
import com.footbolic.api.annotation.RoleCode;
import com.footbolic.api.authorization.service.AuthorizationService;
import com.footbolic.api.common.entity.BaseResponse;
import com.footbolic.api.common.entity.ErrorResponse;
import com.footbolic.api.common.entity.SuccessResponse;
import com.footbolic.api.menu.dto.MenuDto;
import com.footbolic.api.menu.service.MenuService;
import com.footbolic.api.role.dto.RoleDto;
import com.footbolic.api.role.service.RoleService;
import com.footbolic.api.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "메뉴 API")
@RequestMapping("/menus")
@RequiredArgsConstructor
@RestController
@Slf4j
public class MenuController {

    private final MenuService menuService;

    private final AuthorizationService authorizationService;

    private final RoleService roleService;

    private final JwtUtil jwtUtil;

    @Operation(summary = "메뉴 목록 조회", description = "메뉴 목록을 트리 형태로 조회")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public SuccessResponse getMenuList() {
        List<MenuDto> menus = menuService.findAll();

        Map<String, Object> result = new HashMap<>();
        result.put("menus", menus);

        return new SuccessResponse(result);
    }

    @Operation(summary = "회원이 권한을 가진 메뉴 목록 조회", description = "회원이 권한을 가진 메뉴 목록을 트리 형태로 조회한다")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/public")
    public SuccessResponse getMenuListByAuth(
            HttpServletRequest request
    ) {
        String accessToken = jwtUtil.extractAccessToken(request);

        List<RoleDto> roles = accessToken == null ? roleService.findDefaultRoles()
                : jwtUtil.resolveAccessToken(accessToken).getRoles();

        List<MenuDto> menus = menuService.findAllByAuthorizations(
                authorizationService.findAllByRoleIds(
                        roles.stream().map(RoleDto::getId).toList()
                )
        );

        Map<String, Object> result = new HashMap<>();
        result.put("menus", menus);

        return new SuccessResponse(result);
    }

    @Operation(summary = "메뉴 생성", description = "파라미터로 전달 받은 메뉴를 생성")
    @Parameter(name = "menu", description = "생성할 메뉴 객체", required = true)
    @RoleCheck(codes = {
//            @RoleCode(code = RoleCode.ROLE_SYS_MNG),
            @RoleCode(code = RoleCode.ROLE_MENU_MNG)
    })
    @PostMapping
    public ResponseEntity<BaseResponse> createMenu(
            @RequestBody @Valid MenuDto menu
    ) {
        MenuDto createdMenu = menuService.saveMenu(menu);

        Map<String, Object> result = new HashMap<>();
        result.put("createdMenu", createdMenu);

        return ResponseEntity.ok(new SuccessResponse(result));
    }

    @Operation(summary = "메뉴 단건 조회", description = "전달 받은 식별번호를 가진 메뉴 조회")
    @Parameter(name = "id", description = "메뉴 식별번호", required = true)
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getMenu(
            @PathVariable(name = "id") String id
    ) {
        if (id == null || id.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("유효하지 않은 메뉴 식별번호입니다."));
        } else {
            MenuDto menu = menuService.findById(id);

            if (menu != null) {
                Map<String, Object> result = new HashMap<>();
                result.put("menu", menu);

                return ResponseEntity.ok(new SuccessResponse(result));
            } else {
                return ResponseEntity.badRequest().body(new ErrorResponse("존재하지 않는 메뉴입니다."));
            }
        }
    }

    @Operation(summary = "메뉴 수정", description = "파라미터로 전달 받은 메뉴를 수정")
    @Parameter(name = "menu", description = "수정할 메뉴 객체", required = true)
    @PatchMapping
    public ResponseEntity<BaseResponse> updateMenu(
            @RequestBody @Valid MenuDto menu
    ) {
        if (menu.getId() == null || menu.getId().isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("유효하지 않은 메뉴 식별번호입니다."));
        } else if (menuService.existsById(menu.getId())) {
            MenuDto updatedMenu = menuService.saveMenu(menu);

            Map<String, Object> result = new HashMap<>();
            result.put("updatedMenu", updatedMenu);

            return ResponseEntity.ok(new SuccessResponse(result));
        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse("존재하지 않는 메뉴입니다."));
        }
    }

    @Operation(summary = "메뉴 삭제", description = "제공된 식별번호를 가진 메뉴를 삭제")
    @Parameter(name = "menu", description = "삭제할 메뉴 객체", required = true)
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteMenu(
            @PathVariable(name = "id") String id
    ) {
        if (id == null || id.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("유효하지 않은 메뉴 식별번호입니다."));
        } else if (menuService.existsById(id)) {
            try {
                menuService.deleteMenu(id);
            } catch (DataIntegrityViolationException e) {
                return ResponseEntity.badRequest().body(new ErrorResponse("메뉴를 사용하는 권한이 존재합니다."));
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("id", id);

            return ResponseEntity.ok(new SuccessResponse(result));
        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse("존재하지 않는 메뉴입니다."));
        }
    }
}
