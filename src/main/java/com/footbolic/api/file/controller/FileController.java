package com.footbolic.api.file.controller;

import com.footbolic.api.annotation.RoleCheck;
import com.footbolic.api.annotation.RoleCode;
import com.footbolic.api.common.entity.BaseResponse;
import com.footbolic.api.common.entity.ErrorResponse;
import com.footbolic.api.common.entity.SuccessResponse;
import com.footbolic.api.file.dto.FileDto;
import com.footbolic.api.file.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "파일 API")
@RequestMapping("/files")
@RequiredArgsConstructor
@RestController
@Slf4j
public class FileController {

    private final FileService fileService;

    @Operation(summary = "파일 생성", description = "파라미터로 전달 받은 파일을 생성한다.")
    @Parameter(name = "file", description = "생성할 파일 객체", required = true)
    @RoleCheck(codes = {
            @RoleCode(code = RoleCode.ROLE_USER)
    })
    @PostMapping
    public ResponseEntity<BaseResponse> createFile(
            @RequestPart(name = "file") MultipartFile file
    ) {
        Map<String, Object> result = new HashMap<>();
        FileDto createdFile = fileService.save(file);
        result.put("createdFile", createdFile);

        return ResponseEntity.ok(new SuccessResponse(result));
    }

    @Operation(summary = "파일 단건 조회", description = "전달 받은 식별번호를 가진 파일을 조회한다.")
    @Parameter(name = "id", description = "파일 식별번호", required = true)
    @GetMapping("/public/images/{id}")
    public Resource getImageFile(
            @PathVariable(name = "id") String id
    ) {
        FileDto file = fileService.findById(id);
        return file != null ? fileService.getFile(file) : null;
    }

    @Operation(summary = "파일 단건 조회", description = "전달 받은 식별번호를 가진 파일을 조회한다.")
    @Parameter(name = "id", description = "파일 식별번호", required = true)
    @GetMapping("/public/attachments/{id}")
    public ResponseEntity<Resource> getAttachFile(
            @PathVariable(name = "id") String id
    ) {
        FileDto file = fileService.findById(id);

        if (file != null) {
            UrlResource resource = fileService.getFile(file);
            String encodedFileName = UriUtils.encode(file.getOriginalName() + "." + file.getExtension(), StandardCharsets.UTF_8);

            String contentDisposition = "attachment; filename=\"" + encodedFileName + "\"";
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition).body(resource);
        } else {
            return null;
        }
    }

    @Operation(summary = "파일 삭제", description = "제공된 식별번호를 가진 파일을 삭제한다.")
    @Parameter(name = "file", description = "수정할 파일 객체", required = true)
    @RoleCheck(codes = {
            @RoleCode(code = RoleCode.ROLE_USER)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteFile(
            @PathVariable(name = "id") String id,
            Authentication authentication
    ) {
        if (id == null || id.isBlank()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("유효하지 않은 파일 식별번호입니다."));
        } else if (fileService.existsById(id)) {
            FileDto target = fileService.findById(id);

            String memberId = authentication.getCredentials().toString();
            List<String> memberRoleCodes = authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            if (target.getCreateMemberId().equals(memberId) || memberRoleCodes.contains(RoleCode.ROLE_SYS_MNG)) {
                fileService.deleteFile(id);

                Map<String, String> result = new HashMap<>();
                result.put("id", id);

                return ResponseEntity.ok(new SuccessResponse(result));
            } else {
                return ResponseEntity.badRequest().body(new ErrorResponse("삭제할 권한이 없는 파일입니다."));
            }
        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse("조회된 파일이 없습니다."));
        }
    }
}
