package com.footbolic.api.image.controller;

import com.footbolic.api.annotation.RoleCheck;
import com.footbolic.api.annotation.RoleCode;
import com.footbolic.api.common.entity.BaseResponse;
import com.footbolic.api.common.entity.ErrorResponse;
import com.footbolic.api.common.entity.SuccessResponse;
import com.footbolic.api.image.dto.ImageDto;
import com.footbolic.api.image.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "이미지 API")
@RequestMapping("/images")
@RequiredArgsConstructor
@RestController
@Slf4j
public class ImageController {

    private final ImageService imageService;

    @Operation(summary = "이미지 목록 조회", description = "이미지 목록을 page 단위로 조회")
    @ResponseStatus(HttpStatus.OK)
    @RoleCheck(codes = {
            @RoleCode(code = RoleCode.ROLE_USER)
    })
    @GetMapping
    public SuccessResponse getImageList(
            @RequestParam(name = "commentId", required = false) String commentId
    ) {
        Map<String, Object> result = new HashMap<>();
        result.put("images", imageService.findAll(commentId));
        result.put("size", imageService.count(commentId));

        return new SuccessResponse(result);
    }

    @Operation(summary = "이미지 생성", description = "파라미터로 전달 받은 이미지을 생성한다.")
    @Parameter(name = "image", description = "생성할 이미지 객체", required = true)
    @RoleCheck(codes = {
            @RoleCode(code = RoleCode.ROLE_USER)
    })
    @PostMapping
    public ResponseEntity<BaseResponse> createImage(
            @RequestBody @Valid ImageDto image
    ) {
        ImageDto createdImage = imageService.saveImage(image);

        Map<String, Object> result = new HashMap<>();
        result.put("createdImage", createdImage);

        return ResponseEntity.ok(new SuccessResponse(result));
    }

    @Operation(summary = "이미지 단건 조회", description = "전달 받은 식별번호를 가진 이미지을 조회한다.")
    @Parameter(name = "id", description = "이미지 식별번호", required = true)
    @RoleCheck(codes = {
            @RoleCode(code = RoleCode.ROLE_USER)
    })
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getImage(
            @PathVariable(name = "id") String id
    ) {
        ImageDto image = imageService.findById(id);

        if (image != null) {

            Map<String, Object> result = new HashMap<>();
            result.put("image", image);

            return ResponseEntity.ok(new SuccessResponse(result));
        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse("조회된 이미지이 없습니다."));
        }
    }

    @Operation(summary = "이미지 수정", description = "파라미터로 전달 받은 이미지을 수정한다.")
    @Parameter(name = "image", description = "수정할 이미지 객체", required = true)
    @RoleCheck(codes = {
            @RoleCode(code = RoleCode.ROLE_USER)
    })
    @PatchMapping
    public ResponseEntity<BaseResponse> updateImage(
            @RequestBody @Valid ImageDto image,
            Authentication authentication
    ) {
        if (image.getId() == null || image.getId().isBlank()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("유효하지 않은 이미지 정보입니다."));
        } else if (imageService.existsById(image.getId())) {
            ImageDto target = imageService.findById(image.getId());

            String memberId = authentication.getCredentials().toString();
            List<String> memberRoleCodes = authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            if (target.getCreateMemberId().equals(memberId) || memberRoleCodes.contains(RoleCode.ROLE_SYS_MNG)) {

                ImageDto updatedImage = imageService.saveImage(image);

                Map<String, Object> result = new HashMap<>();
                result.put("updatedImage", updatedImage);

                return ResponseEntity.ok(new SuccessResponse(result));
            } else {
                return ResponseEntity.badRequest().body(new ErrorResponse("수정할 권한이 없는 이미지입니다."));
            }
        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse("조회된 이미지이 없습니다."));
        }
    }

    @Operation(summary = "이미지 삭제", description = "제공된 식별번호를 가진 이미지을 삭제한다.")
    @Parameter(name = "image", description = "수정할 이미지 객체", required = true)
    @RoleCheck(codes = {
            @RoleCode(code = RoleCode.ROLE_USER)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteImage(
            @PathVariable(name = "id") String id,
            Authentication authentication
    ) {
        if (id == null || id.isBlank()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("유효하지 않은 이미지 식별번호입니다."));
        } else if (imageService.existsById(id)) {
            ImageDto target = imageService.findById(id);

            String memberId = authentication.getCredentials().toString();
            List<String> memberRoleCodes = authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            if (target.getCreateMemberId().equals(memberId) || memberRoleCodes.contains(RoleCode.ROLE_SYS_MNG)) {
                imageService.deleteImage(id);

                Map<String, String> result = new HashMap<>();
                result.put("id", id);

                return ResponseEntity.ok(new SuccessResponse(result));
            } else {
                return ResponseEntity.badRequest().body(new ErrorResponse("삭제할 권한이 없는 이미지입니다."));
            }
        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse("조회된 이미지이 없습니다."));
        }
    }
}
