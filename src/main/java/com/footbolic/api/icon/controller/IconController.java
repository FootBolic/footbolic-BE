package com.footbolic.api.icon.controller;

import com.footbolic.api.annotation.RoleCheck;
import com.footbolic.api.annotation.RoleCode;
import com.footbolic.api.common.entity.BaseResponse;
import com.footbolic.api.common.entity.ErrorResponse;
import com.footbolic.api.common.entity.SuccessResponse;
import com.footbolic.api.icon.dto.IconDto;
import com.footbolic.api.icon.service.IconService;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "아이콘 API")
@RequestMapping("/icons")
@RequiredArgsConstructor
@RestController
@Slf4j
public class IconController {

    private final IconService iconService;

    @Operation(summary = "아이콘 목록 조회", description = "아이콘 목록을 page 단위로 조회")
    @ResponseStatus(HttpStatus.OK)
    @RoleCheck(codes = {
            @RoleCode(code = RoleCode.ROLE_USER)
    })
    @GetMapping
    public SuccessResponse getIconList(
            Pageable pageable,
            @RequestParam(name = "searchTitle", required = false) String searchTitle,
            @RequestParam(name = "searchCode", required = false) String searchCode
    ) {
        Map<String, Object> result = new HashMap<>();
        result.put("icons", iconService.findAll(pageable, searchTitle, searchCode));
        result.put("size", iconService.count(searchTitle, searchCode));

        return new SuccessResponse(result);
    }

    @GetMapping("/all")
    public SuccessResponse getAllIcons() {
        Map<String, Object> result = new HashMap<>();
        result.put("icons", iconService.findAll());
        result.put("size", iconService.count(null, null));

        return new SuccessResponse(result);
    }

    @Operation(summary = "아이콘 생성", description = "파라미터로 전달 받은 아이콘을 생성한다.")
    @Parameter(name = "icon", description = "생성할 아이콘 객체", required = true)
    @RoleCheck(codes = {
            @RoleCode(code = RoleCode.ROLE_USER)
    })
    @PostMapping
    public ResponseEntity<BaseResponse> createIcon(
            @RequestBody @Valid IconDto icon
    ) {
        IconDto createdIcon = iconService.save(icon);

        Map<String, Object> result = new HashMap<>();
        result.put("createdIcon", createdIcon);

        return ResponseEntity.ok(new SuccessResponse(result));
    }

    @Operation(summary = "아이콘 단건 조회", description = "전달 받은 식별번호를 가진 아이콘을 조회한다.")
    @Parameter(name = "id", description = "아이콘 식별번호", required = true)
    @RoleCheck(codes = {
            @RoleCode(code = RoleCode.ROLE_USER)
    })
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getIcon(
            @PathVariable(name = "id") String id
    ) {
        IconDto icon = iconService.findById(id);

        if (icon != null) {

            Map<String, Object> result = new HashMap<>();
            result.put("icon", icon);

            return ResponseEntity.ok(new SuccessResponse(result));
        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse("조회된 아이콘이 없습니다."));
        }
    }

    @Operation(summary = "아이콘 수정", description = "파라미터로 전달 받은 아이콘을 수정한다.")
    @Parameter(name = "icon", description = "수정할 아이콘 객체", required = true)
    @RoleCheck(codes = {
            @RoleCode(code = RoleCode.ROLE_USER)
    })
    @PatchMapping
    public ResponseEntity<BaseResponse> updateIcon(
            @RequestBody @Valid IconDto icon,
            Authentication authentication
    ) {
        if (icon.getId() == null || icon.getId().isBlank()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("유효하지 않은 아이콘 정보입니다."));
        } else if (iconService.existsById(icon.getId())) {
            IconDto target = iconService.findById(icon.getId());

            String memberId = authentication.getCredentials().toString();
            List<String> memberRoleCodes = authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            if (target.getCreateMemberId().equals(memberId) || memberRoleCodes.contains(RoleCode.ROLE_SYS_MNG)) {
                IconDto updatedIcon = iconService.save(icon);

                Map<String, Object> result = new HashMap<>();
                result.put("updatedIcon", updatedIcon);

                return ResponseEntity.ok(new SuccessResponse(result));
            } else {
                return ResponseEntity.badRequest().body(new ErrorResponse("수정할 권한이 없는 아이콘입니다."));
            }
        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse("조회된 아이콘이 없습니다."));
        }
    }

    @Operation(summary = "아이콘 삭제", description = "제공된 식별번호를 가진 아이콘을 삭제한다.")
    @Parameter(name = "icon", description = "수정할 아이콘 객체", required = true)
    @RoleCheck(codes = {
            @RoleCode(code = RoleCode.ROLE_USER)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteIcon(
            @PathVariable(name = "id") String id,
            Authentication authentication
    ) {
        if (id == null || id.isBlank()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("유효하지 않은 아이콘 식별번호입니다."));
        } else if (iconService.existsById(id)) {
            IconDto target = iconService.findById(id);

            String memberId = authentication.getCredentials().toString();
            List<String> memberRoleCodes = authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            if (target.getCreateMemberId().equals(memberId) || memberRoleCodes.contains(RoleCode.ROLE_SYS_MNG)) {
                iconService.delete(id);

                Map<String, String> result = new HashMap<>();
                result.put("id", id);

                return ResponseEntity.ok(new SuccessResponse(result));
            } else {
                return ResponseEntity.badRequest().body(new ErrorResponse("수정할 권한이 없는 아이콘입니다."));
            }
        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse("조회된 아이콘이 없습니다."));
        }
    }
}
