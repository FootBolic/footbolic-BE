package com.footbolic.api.reply.controller;

import com.footbolic.api.annotation.RoleCheck;
import com.footbolic.api.annotation.RoleCode;
import com.footbolic.api.common.entity.BaseResponse;
import com.footbolic.api.common.entity.ErrorResponse;
import com.footbolic.api.common.entity.SuccessResponse;
import com.footbolic.api.reply.dto.ReplyDto;
import com.footbolic.api.reply.service.ReplyService;
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

@Tag(name = "답글 API")
@RequestMapping("/replies")
@RequiredArgsConstructor
@RestController
@Slf4j
public class ReplyController {

    private final ReplyService replyService;

    @Operation(summary = "답글 목록 조회", description = "답글 목록을 page 단위로 조회")
    @ResponseStatus(HttpStatus.OK)
    @RoleCheck(codes = {
            @RoleCode(code = RoleCode.ROLE_USER)
    })
    @GetMapping
    public SuccessResponse getReplyList(
            @RequestParam(name = "postId", required = false) String postId
    ) {
        Map<String, Object> result = new HashMap<>();
        result.put("replies", replyService.findAll(postId));
        result.put("size", replyService.count(postId));

        return new SuccessResponse(result);
    }

    @Operation(summary = "답글 생성", description = "파라미터로 전달 받은 답글을 생성한다.")
    @Parameter(name = "reply", description = "생성할 답글 객체", required = true)
    @RoleCheck(codes = {
            @RoleCode(code = RoleCode.ROLE_USER)
    })
    @PostMapping
    public ResponseEntity<BaseResponse> createReply(
            @RequestBody @Valid ReplyDto reply
    ) {
        ReplyDto createdReply = replyService.saveReply(reply);

        Map<String, Object> result = new HashMap<>();
        result.put("createdReply", createdReply);

        return ResponseEntity.ok(new SuccessResponse(result));
    }

    @Operation(summary = "답글 단건 조회", description = "전달 받은 식별번호를 가진 답글을 조회한다.")
    @Parameter(name = "id", description = "답글 식별번호", required = true)
    @RoleCheck(codes = {
            @RoleCode(code = RoleCode.ROLE_USER)
    })
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getReply(
            @PathVariable(name = "id") String id
    ) {
        ReplyDto reply = replyService.findById(id);

        if (reply != null) {

            Map<String, Object> result = new HashMap<>();
            result.put("reply", reply);

            return ResponseEntity.ok(new SuccessResponse(result));
        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse("조회된 답글이 없습니다."));
        }
    }

    @Operation(summary = "답글 수정", description = "파라미터로 전달 받은 답글을 수정한다.")
    @Parameter(name = "reply", description = "수정할 답글 객체", required = true)
    @RoleCheck(codes = {
            @RoleCode(code = RoleCode.ROLE_USER)
    })
    @PatchMapping
    public ResponseEntity<BaseResponse> updateReply(
            @RequestBody @Valid ReplyDto reply,
            Authentication authentication
    ) {
        if (reply.getId() == null || reply.getId().isBlank()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("유효하지 않은 답글 정보입니다."));
        } else if (replyService.existsById(reply.getId())) {
            ReplyDto target = replyService.findById(reply.getId());

            String memberId = authentication.getCredentials().toString();
            List<String> memberRoleCodes = authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            if (target.getCreateMemberId().equals(memberId) || memberRoleCodes.contains(RoleCode.ROLE_SYS_MNG)) {

                ReplyDto updatedReply = replyService.saveReply(reply);

                Map<String, Object> result = new HashMap<>();
                result.put("updatedReply", updatedReply);

                return ResponseEntity.ok(new SuccessResponse(result));
            } else {
                return ResponseEntity.badRequest().body(new ErrorResponse("수정할 권한이 없는 답글입니다."));
            }
        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse("조회된 답글이 없습니다."));
        }
    }

    @Operation(summary = "답글 삭제", description = "제공된 식별번호를 가진 답글을 삭제한다.")
    @Parameter(name = "reply", description = "수정할 답글 객체", required = true)
    @RoleCheck(codes = {
            @RoleCode(code = RoleCode.ROLE_USER)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteReply(
            @PathVariable(name = "id") String id,
            Authentication authentication
    ) {
        if (id == null || id.isBlank()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("유효하지 않은 답글 식별번호입니다."));
        } else if (replyService.existsById(id)) {
            ReplyDto target = replyService.findById(id);

            String memberId = authentication.getCredentials().toString();
            List<String> memberRoleCodes = authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            if (target.getCreateMemberId().equals(memberId) || memberRoleCodes.contains(RoleCode.ROLE_SYS_MNG)) {
                replyService.deleteReply(id);

                Map<String, String> result = new HashMap<>();
                result.put("id", id);

                return ResponseEntity.ok(new SuccessResponse(result));
            } else {
                return ResponseEntity.badRequest().body(new ErrorResponse("삭제할 권한이 없는 답글입니다."));
            }
        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse("조회된 답글이 없습니다."));
        }
    }
}
