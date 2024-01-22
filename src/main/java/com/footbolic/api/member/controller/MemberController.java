package com.footbolic.api.member.controller;

import com.footbolic.api.common.entity.BaseResponse;
import com.footbolic.api.common.entity.ErrorResponse;
import com.footbolic.api.common.entity.SuccessResponse;
import com.footbolic.api.member.service.MemberService;
import com.footbolic.api.member.dto.MemberDto;
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

@Tag(name = "회원 API")
@RequestMapping("/members")
@RequiredArgsConstructor
@RestController
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원 목록 조회", description = "회원 목록을 page 단위로 조회")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public SuccessResponse getMemberList(Pageable pageable) {
        return new SuccessResponse(memberService.findAllMembers(pageable));
    }

    @Operation(summary = "회원 생성", description = "파라미터로 전달 받은 회원를 생성")
    @Parameter(name = "member", description = "생성할 회원 객체", required = true)
    @PostMapping
    public ResponseEntity<BaseResponse> createMember(
            @RequestBody @Valid MemberDto member
    ) {
        MemberDto createdMember = memberService.saveMember(member);
        return ResponseEntity.ok(new SuccessResponse(createdMember));
    }

    @Operation(summary = "회원 단건 조회", description = "전달 받은 식별번호를 가진 회원 조회")
    @Parameter(name = "id", description = "회원 식별번호", required = true)
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getMember(
            @PathVariable(name = "id") String id
    ) {
        if (id == null || id.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorResponse(
                "Provided member id is invalid"
            ));
        } else {
            MemberDto member = memberService.findById(id);

            if (member != null) {
                return ResponseEntity.ok(new SuccessResponse(member));
            } else {
                return ResponseEntity.badRequest().body(new ErrorResponse(
                    "Member with provided id does not exist"
                ));
            }
        }
    }

    @Operation(summary = "회원 수정", description = "파라미터로 전달 받은 회원를 수정")
    @Parameter(name = "member", description = "수정할 회원 객체", required = true)
    @PatchMapping
    public ResponseEntity<BaseResponse> updateMember(
            @RequestBody @Valid MemberDto member
    ) {
        if (member.getId() == null || member.getId().isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorResponse(
                "Provided member id is invalid"
            ));
        } else if (memberService.existsById(member.getId())) {
            MemberDto updatedMember = memberService.saveMember(member);
            return ResponseEntity.ok(new SuccessResponse(updatedMember));
        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse(
                "Provided member does not exist"
            ));
        }
    }

    @Operation(summary = "회원 삭제", description = "제공된 식별번호를 가진 회원 삭제")
    @Parameter(name = "member", description = "수정할 회원 객체", required = true)
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteMember(
            @PathVariable(name = "id") String id
    ) {
        if (id == null || id.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorResponse(
                "Provided member id is invalid"
            ));
        } else if (memberService.existsById(id)) {
            memberService.deleteMember(id);
            return ResponseEntity.ok(new SuccessResponse(null));
        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse(
                "Member with provided id does not exist"
            ));
        }
    }
}
