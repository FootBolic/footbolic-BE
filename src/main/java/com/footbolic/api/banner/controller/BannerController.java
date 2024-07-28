package com.footbolic.api.banner.controller;

import com.footbolic.api.annotation.RoleCheck;
import com.footbolic.api.annotation.RoleCode;
import com.footbolic.api.common.entity.BaseResponse;
import com.footbolic.api.common.entity.ErrorResponse;
import com.footbolic.api.common.entity.SuccessResponse;
import com.footbolic.api.banner.dto.BannerDto;
import com.footbolic.api.banner.service.BannerService;
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

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Tag(name = "배너 API")
@RequestMapping("/banners")
@RequiredArgsConstructor
@RestController
@Slf4j
public class BannerController {

    private final BannerService bannerService;

    @Operation(summary = "배너 목록 조회", description = "배너 목록을 page 단위로 조회한다.")
    @Parameter(name = "searchTitle", description = "제목 검색 파라미터")
    @Parameter(name = "searchMenuId", description = "메뉴 식별번호 검색 파라미터")
    @Parameter(name = "size", description = "결과 목록 크기")
    @Parameter(name = "page", description = "결과 목록 페이지")
    @ResponseStatus(HttpStatus.OK)
    @RoleCheck(codes = {
            @RoleCode(code = RoleCode.ROLE_BANNER_MNG)
    })
    @GetMapping
    public SuccessResponse getBannerList(
            Pageable pageable,
            @RequestParam(name = "searchTitle", required = false) String searchTitle,
            @RequestParam(name = "searchDate", required = false) LocalDateTime searchDate
    ) {
        Map<String, Object> result = new HashMap<>();
        result.put("banners", bannerService.findAll(pageable, searchTitle, searchDate));
        result.put("size", bannerService.count(searchTitle, searchDate));

        return new SuccessResponse(result);
    }

    @Operation(summary = "게시중인 배너 목록 조회", description = "메인페이지에 게시중인 배너 목록을 조회한다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/public")
    public SuccessResponse getPublicBannerList(
            @RequestParam(name = "isMobile") boolean isMobile
    ) {
        Map<String, Object> result = new HashMap<>();
        result.put("banners", bannerService.findPublic(isMobile));

        return new SuccessResponse(result);
    }

    @Operation(summary = "배너 생성", description = "파라미터로 전달 받은 배너을 생성한다.")
    @Parameter(name = "banner", description = "생성할 배너 객체", required = true)
    @RoleCheck(codes = {
            @RoleCode(code = RoleCode.ROLE_BANNER_MNG)
    })
    @PostMapping
    public ResponseEntity<BaseResponse> createBanner(
            @RequestBody @Valid BannerDto banner
    ) {
        BannerDto createdBanner = bannerService.save(banner);

        Map<String, Object> result = new HashMap<>();
        result.put("createdBanner", createdBanner);

        return ResponseEntity.ok(new SuccessResponse(result));
    }

    @Operation(summary = "배너 단건 조회", description = "전달 받은 식별번호를 가진 배너 조회한다.")
    @Parameter(name = "id", description = "배너 식별번호", required = true)
    @RoleCheck(codes = {
            @RoleCode(code = RoleCode.ROLE_BANNER_MNG)
    })
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getBanner(
            @PathVariable(name = "id") String id
    ) {
        BannerDto banner = bannerService.findById(id);

        if (banner != null) {
            Map<String, Object> result = new HashMap<>();
            result.put("banner", banner);

            return ResponseEntity.ok(new SuccessResponse(result));
        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse("조회된 배너이 없습니다."));
        }
    }

    @Operation(summary = "배너 수정", description = "파라미터로 전달 받은 배너을 수정한다.")
    @Parameter(name = "banner", description = "수정할 배너 객체", required = true)
    @RoleCheck(codes = {
            @RoleCode(code = RoleCode.ROLE_BANNER_MNG)
    })
    @PatchMapping
    public ResponseEntity<BaseResponse> updateBanner(
            @RequestBody @Valid BannerDto banner
    ) {
        if (banner.getId() == null || banner.getId().isBlank()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("유효하지 않은 배너정보입니다."));
        } else if (bannerService.existsById(banner.getId())) {
            BannerDto updatedBanner = bannerService.save(banner);

            Map<String, Object> result = new HashMap<>();
            result.put("updatedBanner", updatedBanner);

            return ResponseEntity.ok(new SuccessResponse(result));
        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse("조회된 배너이 없습니다."));
        }
    }

    @Operation(summary = "배너 삭제", description = "제공된 식별번호를 가진 배너 삭제한다.")
    @RoleCheck(codes = {
            @RoleCode(code = RoleCode.ROLE_BANNER_MNG)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteBanner(
            @PathVariable(name = "id") String id
    ) {
        if (id == null || id.isBlank()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("유효하지 않은 배너 식별번호입니다"));
        } else if (bannerService.existsById(id)) {
            try {
                bannerService.deleteById(id);
            } catch (DataIntegrityViolationException e) {
                return ResponseEntity.badRequest().body(new ErrorResponse("배너을 사용하는 메뉴가 존재합니다."));
            }

            Map<String, String> result = new HashMap<>();
            result.put("id", id);

            return ResponseEntity.ok(new SuccessResponse(result));
        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse("조회된 배너이 없습니다."));
        }
    }
}
