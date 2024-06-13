package com.footbolic.api.comment.controller;

import com.footbolic.api.annotation.RoleCheck;
import com.footbolic.api.annotation.RoleCode;
import com.footbolic.api.comment.dto.CommentDto;
import com.footbolic.api.comment.service.CommentService;
import com.footbolic.api.common.entity.BaseResponse;
import com.footbolic.api.common.entity.ErrorResponse;
import com.footbolic.api.common.entity.SuccessResponse;
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

@Tag(name = "댓글 API")
@RequestMapping("/comments")
@RequiredArgsConstructor
@RestController
@Slf4j
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 목록 조회", description = "댓글 목록을 page 단위로 조회")
    @ResponseStatus(HttpStatus.OK)
    @RoleCheck(codes = {
            @RoleCode(code = RoleCode.ROLE_USER)
    })
    @GetMapping
    public SuccessResponse getCommentList(
            @RequestParam(name = "postId", required = false) String postId
    ) {
        Map<String, Object> result = new HashMap<>();
        result.put("comments", commentService.findAll(postId));
        result.put("size", commentService.count(postId));

        return new SuccessResponse(result);
    }

    @Operation(summary = "댓글 생성", description = "파라미터로 전달 받은 댓글을 생성한다.")
    @Parameter(name = "comment", description = "생성할 댓글 객체", required = true)
    @RoleCheck(codes = {
            @RoleCode(code = RoleCode.ROLE_USER)
    })
    @PostMapping
    public ResponseEntity<BaseResponse> createComment(
            @RequestBody @Valid CommentDto comment
    ) {
        CommentDto createdComment = commentService.saveComment(comment);

        Map<String, Object> result = new HashMap<>();
        result.put("createdComment", createdComment);

        return ResponseEntity.ok(new SuccessResponse(result));
    }

    @Operation(summary = "댓글 단건 조회", description = "전달 받은 식별번호를 가진 댓글을 조회한다.")
    @Parameter(name = "id", description = "댓글 식별번호", required = true)
    @RoleCheck(codes = {
            @RoleCode(code = RoleCode.ROLE_USER)
    })
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getComment(
            @PathVariable(name = "id") String id
    ) {
        CommentDto comment = commentService.findById(id);

        if (comment != null) {

            Map<String, Object> result = new HashMap<>();
            result.put("comment", comment);

            return ResponseEntity.ok(new SuccessResponse(result));
        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse("조회된 댓글이 없습니다."));
        }
    }

    @Operation(summary = "댓글 수정", description = "파라미터로 전달 받은 댓글을 수정한다.")
    @Parameter(name = "comment", description = "수정할 댓글 객체", required = true)
    @RoleCheck(codes = {
            @RoleCode(code = RoleCode.ROLE_USER)
    })
    @PatchMapping
    public ResponseEntity<BaseResponse> updateComment(
            @RequestBody @Valid CommentDto comment,
            Authentication authentication
    ) {
        if (comment.getId() == null || comment.getId().isBlank()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("유효하지 않은 댓글 정보입니다."));
        } else if (commentService.existsById(comment.getId())) {
            CommentDto target = commentService.findById(comment.getId());

            String memberId = authentication.getCredentials().toString();
            List<String> memberRoleCodes = authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            if (target.getCreateMemberId().equals(memberId) || memberRoleCodes.contains(RoleCode.ROLE_SYS_MNG)) {

                CommentDto updatedComment = commentService.saveComment(comment);

                Map<String, Object> result = new HashMap<>();
                result.put("updatedComment", updatedComment);

                return ResponseEntity.ok(new SuccessResponse(result));
            } else {
                return ResponseEntity.badRequest().body(new ErrorResponse("수정할 권한이 없는 댓글입니다."));
            }
        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse("조회된 댓글이 없습니다."));
        }
    }

    @Operation(summary = "댓글 삭제", description = "제공된 식별번호를 가진 댓글을 삭제한다.")
    @Parameter(name = "comment", description = "수정할 댓글 객체", required = true)
    @RoleCheck(codes = {
            @RoleCode(code = RoleCode.ROLE_USER)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteComment(
            @PathVariable(name = "id") String id,
            Authentication authentication
    ) {
        if (id == null || id.isBlank()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("유효하지 않은 댓글 식별번호입니다."));
        } else if (commentService.existsById(id)) {
            CommentDto target = commentService.findById(id);

            String memberId = authentication.getCredentials().toString();
            List<String> memberRoleCodes = authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            if (target.getCreateMemberId().equals(memberId) || memberRoleCodes.contains(RoleCode.ROLE_SYS_MNG)) {
                commentService.deleteComment(id);

                Map<String, String> result = new HashMap<>();
                result.put("id", id);

                return ResponseEntity.ok(new SuccessResponse(result));
            } else {
                return ResponseEntity.badRequest().body(new ErrorResponse("삭제할 권한이 없는 댓글입니다."));
            }
        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse("조회된 댓글이 없습니다."));
        }
    }
}
