package com.footbolic.api.sign.controller;

import com.footbolic.api.common.entity.BaseResponse;
import com.footbolic.api.common.entity.ErrorResponse;
import com.footbolic.api.common.entity.SuccessResponse;
import com.footbolic.api.member.dto.MemberDto;
import com.footbolic.api.member.service.MemberService;
import com.footbolic.api.role.service.RoleService;
import com.footbolic.api.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Tag(name = "로그인 API")
@RequestMapping("/sign")
@RequiredArgsConstructor
@RestController
@Slf4j
public class SignController {

    private final MemberService memberService;

    private final RoleService roleService;

    private final JwtUtil jwtUtil;

    @Operation(summary = "로그인 처리 및 토큰 발급", description = "로그인 처리 후 access token과 refresh token을 발급한다.")
    @Parameter(name = "paramMember", description = "플랫폼 정보와 플랫폼에서 발급된 id 정보", required = true)
    @PostMapping("/in")
    public ResponseEntity<BaseResponse> signIn(
            HttpServletResponse response,
            @RequestBody MemberDto paramMember
    ) {
        if (paramMember == null ||
                (paramMember.getIdAtProvider() == null || paramMember.getIdAtProvider().isEmpty()) ||
                (paramMember.getPlatform() == null || paramMember.getPlatform().isEmpty())
        ) {
            return ResponseEntity.badRequest().body(new ErrorResponse("잘못된 회원정보입니다."));
        }

        if (memberService.existsByIdAtPlatform(paramMember.getIdAtProvider(), paramMember.getPlatform())) {
            MemberDto member = memberService.findByIdAtPlatform(paramMember.getIdAtProvider(), paramMember.getPlatform());
            member.setRoles(roleService.findAllByMemberId(member.getId()));

            Map<String, Object> result = new HashMap<>();

            Date accessExpiration = jwtUtil.getAccessExpiration();
            String accessToken = jwtUtil.generateAccessToken(member, accessExpiration);
            result.put("access_token", accessToken);
            result.put("expires_at", LocalDateTime.ofInstant(accessExpiration.toInstant(), ZoneId.systemDefault()));
            result.put("nickname", member.getNickname());

            Date refreshExpiration = jwtUtil.getRefreshExpiration();
            String refreshToken = jwtUtil.generateRefreshToken(member, refreshExpiration);

            jwtUtil.issueRefreshToken(response, refreshToken);

            member.setRefreshToken(refreshToken);
            member.setRefreshTokenExpiresAt(jwtUtil.toLocalDateTime(refreshExpiration));

            memberService.updateTokenInfo(member);

            return ResponseEntity.ok(new SuccessResponse(result));
        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse("잘못된 회원정보입니다."));
        }
    }

    @Operation(summary = "로그아웃 처리", description = "로그아웃 처리를 한다.")
    @PostMapping("/out")
    public ResponseEntity<BaseResponse> signOut(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        MemberDto member = Optional.ofNullable(jwtUtil.extractAccessToken(request))
                .filter(jwtUtil::validateAccessToken)
                .map(jwtUtil::resolveAccessToken)
                .filter(e -> e.getAccessTokenExpiresAt().isAfter(LocalDateTime.now()))
                .filter(e -> e.getRoles() != null && !e.getRoles().isEmpty())
                .orElse(null);

        if (member != null) {
            jwtUtil.removeRefreshToken(response);
            memberService.updateTokenInfo(MemberDto.builder().id(member.getId()).build());

            Map<String, String> result = new HashMap<>();

            result.put("id", member.getId());

            return ResponseEntity.ok(new SuccessResponse(result));
        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse("로그아웃할 회원이 존재하지 않습니다."));
        }
    }

    @Operation(summary = "로그인 상태 확인", description = "Refresh Token 존재 여부를 확인한다.")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/check")
    public BaseResponse check(
            HttpServletRequest request
    ) {
        String refreshToken = jwtUtil.extractRefreshToken(request);

        Map<String, Boolean> result = new HashMap<>();
        result.put("check_result", refreshToken != null && !refreshToken.isEmpty() && jwtUtil.validateRefreshToken(refreshToken));

        return new SuccessResponse(result);
    }

    @Operation(summary = "Access Token 갱신", description = "Refresh Token을 검증한 후 Access Token을 갱신한다.")
    @PostMapping("/renew")
    public ResponseEntity<BaseResponse> renew(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String refreshToken = jwtUtil.extractRefreshToken(request);

        if (refreshToken == null || refreshToken.isEmpty()) {
            jwtUtil.removeRefreshToken(response);
            return ResponseEntity.badRequest().body(new ErrorResponse("유효하지 않은 토큰입니다."));
        }

        MemberDto tokenMember = Optional.of(refreshToken)
                .filter(jwtUtil::validateRefreshToken)
                .map(jwtUtil::resolveRefreshToken)
                .filter(e -> e.getRefreshTokenExpiresAt().isAfter(LocalDateTime.now()))
                .filter(e -> e.getId() != null && !e.getId().isEmpty())
                .orElse(null);

        if (tokenMember == null) {
            jwtUtil.removeRefreshToken(response);
            return ResponseEntity.badRequest().body(new ErrorResponse("회원정보가 없는 토큰입니다."));
        }

        MemberDto member = memberService.findById(tokenMember.getId());

        if (member.getRefreshToken() != null && member.getRefreshToken().equals(refreshToken)) {

            Map<String, Object> result = new HashMap<>();

            Date accessExpiration = jwtUtil.getAccessExpiration();
            String accessToken = jwtUtil.generateAccessToken(member, accessExpiration);

            result.put("access_token", accessToken);
            result.put("expires_at", LocalDateTime.ofInstant(accessExpiration.toInstant(), ZoneId.systemDefault()));
            result.put("nickname", member.getNickname());

            return ResponseEntity.ok(new SuccessResponse(result));
        } else {
            jwtUtil.removeRefreshToken(response);
            memberService.updateTokenInfo(MemberDto.builder().id(member.getId()).build());

            return ResponseEntity.ok(new ErrorResponse("유효하지 않은 토큰입니다."));
        }
    }
}
