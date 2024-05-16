package com.footbolic.api.member.controller;

import com.footbolic.api.common.entity.BaseResponse;
import com.footbolic.api.common.entity.ErrorResponse;
import com.footbolic.api.common.entity.SuccessResponse;
import com.footbolic.api.member.service.MemberService;
import com.footbolic.api.member.dto.MemberDto;
import com.footbolic.api.util.HttpUtil;
import com.footbolic.api.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.HttpURLConnection;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Tag(name = "회원 API")
@RequestMapping("/members")
@RequiredArgsConstructor
@RestController
@Slf4j
public class MemberController {

    private final JwtUtil jwtUtil;

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
        member.setNicknameLastUpdatedAt(LocalDateTime.now());
        return ResponseEntity.ok(new SuccessResponse(memberService.saveMember(member)));
    }

    @Operation(summary = "회원 단건 조회", description = "Access Token으로 회원정보 조회")
    @PostMapping("/me")
    public ResponseEntity<BaseResponse> getMember(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        MemberDto tokenMember = jwtUtil.resolveAccessToken(jwtUtil.extractAccessToken(request));

        if (tokenMember == null || tokenMember.getId() == null || tokenMember.getId().isEmpty()) {
            jwtUtil.removeRefreshToken(response);
            return ResponseEntity.badRequest().body(new ErrorResponse("유효하지 않은 회원정보입니다."));
        }

        MemberDto member = memberService.findById(tokenMember.getId());

        if (member != null) {
            return ResponseEntity.ok(new SuccessResponse(member));
        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse("조회된 회원이 없습니다."));
        }
    }

    @Operation(summary = "회원 존재 여부 조회", description = "전달 받은 API 제공 플랫폼과 식별번호를 가진 회원 존재 여부 조회")
    @Parameter(name = "id", description = "회원 식별번호", required = true)
    @Parameter(name = "platform", description = "로그인 API 제공 플랫폼", required = true)
    @GetMapping("/public/{id}")
    public ResponseEntity<BaseResponse> memberExists(
            @PathVariable(name = "id") String id,
            @RequestParam(name = "platform") String platform
    ) {
        if (id == null || id.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("유효하지 않은 회원 식별번호입니다."));
        }

        Map<String, Boolean> result = new HashMap<>();
        result.put("memberExists", memberService.existsByIdAtPlatform(id, platform));

        return ResponseEntity.ok(new SuccessResponse(result));
    }

    @Operation(summary = "회원 수정", description = "파라미터로 전달 받은 회원을 수정")
    @Parameter(name = "member", description = "수정할 회원 객체", required = true)
    @PatchMapping
    public ResponseEntity<BaseResponse> updateMember(
            @RequestBody @Valid MemberDto member
    ) {
        if (member.getId() == null || member.getId().isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("유효하지 않은 회원 식별번호입니다."));
        } else if (memberService.existsById(member.getId())) {
            MemberDto updatedMember = memberService.saveMember(member);
            return ResponseEntity.ok(new SuccessResponse(updatedMember));
        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse("조회된 회원이 없습니다"));
        }
    }

    @Operation(summary = "Access Token으로 회원 수정", description = "Access Token으로 전달 받은 회원을 파라미터 정보로 수정")
    @Parameter(name = "member", description = "수정할 회원 객체", required = true)
    @PatchMapping("/me")
    public ResponseEntity<BaseResponse> updateTokenMember(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody MemberDto paramMember
    ) {
        MemberDto tokenMember = jwtUtil.resolveAccessToken(jwtUtil.extractAccessToken(request));

        if (tokenMember == null || tokenMember.getId() == null || tokenMember.getId().isEmpty()) {
            jwtUtil.removeRefreshToken(response);
            return ResponseEntity.badRequest().body(new ErrorResponse("유효하지 않은 회원정보입니다."));
        }

        MemberDto member = memberService.findById(tokenMember.getId());

        if (member != null) {
            if (member.getNickname().equals(paramMember.getNickname())) {
                return ResponseEntity.badRequest().body(new ErrorResponse("변경된 정보가 없습니다."));
            } else {
                member.setNickname(paramMember.getNickname());
                member.setNicknameLastUpdatedAt(LocalDateTime.now());
                MemberDto updatedMember = memberService.saveMember(member);

                return ResponseEntity.ok(new SuccessResponse(updatedMember));
            }
        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse("조회된 회원이 없습니다"));
        }
    }

    @Operation(summary = "회원 삭제", description = "제공된 식별번호를 가진 회원 삭제")
    @Parameter(name = "member", description = "수정할 회원 객체", required = true)
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteMember(
            @PathVariable(name = "id") String id
    ) {
        if (id == null || id.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("유효하지 않은 회원 식별번호입니다."));
        } else if (memberService.existsById(id)) {
            memberService.deleteMember(id);
            return ResponseEntity.ok(new SuccessResponse(null));
        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse("조회된 회원이 없습니다."));
        }
    }

    @Operation(summary = "네이버 API에 토큰 발급 요청", description = "네이버 API에 토큰 발급 요청")
    @Parameter(name = "code", description = "네이버 API로부터 받은 인증코드", required = true)
    @PostMapping("/public/oauth/naver")
    public ResponseEntity<BaseResponse> authenticateFromNaver(
            @RequestParam(name = "code") String code
    ) {
        HttpUtil httpUtil = new HttpUtil();

        HttpURLConnection conn  = httpUtil.getConn(
                "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code" +
                        "&client_id=Nw3kwPg8dk6L_kUkXPkk&client_secret=nu5DKFMki2&code="+code,
                "GET"
        );

        String str = httpUtil.getHttpResponse(conn);

        return ResponseEntity.ok(new SuccessResponse(str));
    }

    @Operation(summary = "네이버 API에 사용자 정보 요청", description = "네이버 API에 사용자 정보 요청")
    @Parameter(name = "token_type", description = "네이버 API로부터 받은 토큰의 타입", required = true)
    @Parameter(name = "access_token", description = "네이버 API로부터 받은 토큰", required = true)
    @PostMapping("/public/oauth/naver/user-info")
    public ResponseEntity<BaseResponse> getUserInfo(
            @RequestParam(name = "token_type") String tokenType,
            @RequestParam(name = "access_token") String accessToken
    ) {
        HttpUtil httpUtil = new HttpUtil();

        HttpURLConnection conn  = httpUtil.getConn(
                "https://openapi.naver.com/v1/nid/me",
                "GET"
        );

        conn.setRequestProperty("Authorization", tokenType + " " + accessToken);

        String str = httpUtil.getHttpResponse(conn);

        return ResponseEntity.ok(new SuccessResponse(str));
    }
}