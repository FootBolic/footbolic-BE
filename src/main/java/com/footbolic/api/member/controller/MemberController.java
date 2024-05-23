package com.footbolic.api.member.controller;

import com.footbolic.api.common.entity.BaseResponse;
import com.footbolic.api.common.entity.ErrorResponse;
import com.footbolic.api.common.entity.SuccessResponse;
import com.footbolic.api.member.service.MemberService;
import com.footbolic.api.member.dto.MemberDto;
import com.footbolic.api.role.service.RoleService;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Tag(name = "회원 API")
@RequestMapping("/members")
@RequiredArgsConstructor
@RestController
@Slf4j
public class MemberController {

    private final RoleService roleService;

    private final MemberService memberService;

    private final JwtUtil jwtUtil;

    private final HttpUtil httpUtil;

    @Value("${auth.platform.naver}")
    private String NAVER;

    @Value("${auth.platform.kakao}")
    private String KAKAO;

    @Value("${auth.platform.naver.client_id}")
    private String NAVER_CLIENT_ID;

    @Value("${auth.platform.naver.client_secret}")
    private String NAVER_CLIENT_SECRET;

    @Value("${auth.platform.kakao.admin_key}")
    private String KAKAO_ADMIN_KEY;

    @Operation(summary = "회원 목록 조회", description = "회원 목록을 page 단위로 조회")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public SuccessResponse getMemberList(Pageable pageable) {
        Map<String, Object> result = new HashMap<>();
        result.put("members", memberService.findAll(pageable));
        result.put("size", memberService.count());

        return new SuccessResponse(result);
    }

    @Operation(summary = "회원 생성", description = "파라미터로 전달 받은 회원를 생성")
    @Parameter(name = "member", description = "생성할 회원 객체", required = true)
    @PostMapping
    public ResponseEntity<BaseResponse> createMember(
            @RequestBody @Valid MemberDto member
    ) {
        member.setNicknameLastUpdatedAt(LocalDateTime.now());
        member.setRoleId(roleService.findDefaultRole().getId());
        return ResponseEntity.ok(new SuccessResponse(memberService.saveMember(member)));
    }

    @Operation(summary = "회원 단건 조회", description = "회원 식별번호로 회원정보 조회")
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getMember(
            @PathVariable(name = "id") String id
    ) {
        MemberDto member = memberService.findById(id);

        if (member != null) {
            return ResponseEntity.ok(new SuccessResponse(member));
        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse("조회된 회원이 없습니다."));
        }
    }

    @Operation(summary = "회원 단건 조회", description = "Access Token으로 회원정보 조회")
    @PostMapping("/me")
    public ResponseEntity<BaseResponse> getTokenMember(
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

    @Operation(summary = "회원 회원가입 플랫폼 조회", description = "Access Token에 존재하는 회원의 회원가입 플랫폼 조회")
    @PostMapping("/me/platform")
    public ResponseEntity<BaseResponse> checkPlatform(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        MemberDto tokenMember = jwtUtil.resolveAccessToken(jwtUtil.extractAccessToken(request));

        if ( tokenMember == null || tokenMember.getId() == null || tokenMember.getId().isBlank()) {
            jwtUtil.removeRefreshToken(response);
            return ResponseEntity.badRequest().body(new ErrorResponse("유효하지 않은 회원정보입니다."));
        }

        MemberDto member = memberService.findById(tokenMember.getId());

        if (member == null || member.getPlatform() == null || member.getPlatform().isBlank()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("유효하지 않은 회원정보입니다."));
        } else {
            Map<String, String> result = new HashMap<>();
            result.put("platform", member.getPlatform());

            return ResponseEntity.ok(new SuccessResponse(result));
        }
    }

    @Operation(summary = "회원 수정", description = "파라미터로 전달 받은 회원을 수정")
    @Parameter(name = "member", description = "수정할 회원 객체", required = true)
    @PatchMapping
    public ResponseEntity<BaseResponse> updateMember(
            @RequestBody @Valid MemberDto paramMember
    ) {
        if (paramMember.getId() == null || paramMember.getId().isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("유효하지 않은 회원 식별번호입니다."));
        } else if (memberService.existsById(paramMember.getId())) {
            MemberDto member = memberService.findById(paramMember.getId());

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
            MemberDto member = memberService.findById(id);

            try {
                HttpURLConnection conn;

                if (member.getPlatform().equals(NAVER)) {
                    return ResponseEntity.ok(new ErrorResponse("네이버를 통해 가입한 회원은 직접 탈퇴만 가능합니다."));
                } else if (member.getPlatform().equals(KAKAO)) {
                    conn = httpUtil.getConn(
                            "https://kapi.kakao.com/v1/user/unlink",
                            "POST"
                    );

                    conn.setRequestProperty("Authorization", "KakaoAK " + KAKAO_ADMIN_KEY);
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                    conn.setDoOutput(true);

                    String paramData = "target_id_type=user_id&target_id=" + member.getIdAtProvider();

                    byte[] data = paramData.getBytes(StandardCharsets.UTF_8);
                    try (OutputStream os = conn.getOutputStream()) {
                        os.write(data);
                    }
                } else {
                    return ResponseEntity.badRequest().body(new ErrorResponse("유효하지 않은 회원정보입니다."));
                }

                String httpResponse = httpUtil.getHttpResponse(conn);

                if (httpResponse == null) {
                    return ResponseEntity.badRequest().body(new ErrorResponse("서버와의 통신이 원활하지 않습니다."));
                } else {
                    memberService.withdraw(member);

                    Map<String, String> result = new HashMap<>();
                    result.put("id", member.getId());

                    return ResponseEntity.ok(new SuccessResponse(result));
                }
            } catch (IOException e) {
                return ResponseEntity.badRequest().body(new ErrorResponse("서버와 통신중 에러가 발생하였습니다."));
            }
        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse("조회된 회원이 없습니다."));
        }
    }

    @Operation(summary = "Access Token으로 회원 탈퇴 처리", description = "Access Token으로 회원 탈퇴 처리")
    @Parameter(name = "access_token", description = "로그인 API 제공자로부터 받은 Access Token", required = true)
    @DeleteMapping("/me")
    public ResponseEntity<BaseResponse> deleteTokenMember(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(name = "access_token") String providerToken
    ) {
        MemberDto tokenMember = jwtUtil.resolveAccessToken(jwtUtil.extractAccessToken(request));

        if ( tokenMember == null ||
                tokenMember.getIdAtProvider() == null || tokenMember.getIdAtProvider().isBlank()
                || tokenMember.getPlatform() == null || tokenMember.getPlatform().isBlank() ) {
            jwtUtil.removeRefreshToken(response);
            return ResponseEntity.badRequest().body(new ErrorResponse("유효하지 않은 회원정보입니다."));
        }

        MemberDto member = memberService.findById(tokenMember.getId());

        HttpURLConnection conn;

        if (member.getPlatform().equals(NAVER)) {
            conn = httpUtil.getConn(
                    "https://nid.naver.com/oauth2.0/token?grant_type=delete" +
                            "&client_id=" + NAVER_CLIENT_ID +
                            "&client_secret=" + NAVER_CLIENT_SECRET +
                            "&access_token=" + providerToken,
                    "GET"
            );
        } else if (member.getPlatform().equals(KAKAO)) {
            conn = httpUtil.getConn(
                    "https://kapi.kakao.com/v1/user/unlink",
                    "POST"
            );

            conn.setRequestProperty("Authorization", "Bearer " + providerToken);
        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse("유효하지 않은 회원정보입니다."));
        }

        String httpResponse = httpUtil.getHttpResponse(conn);

        if (httpResponse == null) {
            return ResponseEntity.badRequest().body(new ErrorResponse("서버와의 통신이 원활하지 않습니다."));
        } else {
            memberService.withdraw(member);
            jwtUtil.removeRefreshToken(response);

            Map<String, String> result = new HashMap<>();
            result.put("id", member.getId());

            return ResponseEntity.ok(new SuccessResponse(result));
        }
    }

    @Operation(summary = "네이버 API에 토큰 발급 요청", description = "네이버 API에 토큰 발급 요청")
    @Parameter(name = "code", description = "네이버 API로부터 받은 인증코드", required = true)
    @PostMapping("/public/oauth/naver")
    public ResponseEntity<BaseResponse> authenticateFromNaver(
            @RequestParam(name = "code") String code
    ) {
        HttpURLConnection conn  = httpUtil.getConn(
                "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code" +
                        "&client_id=" + NAVER_CLIENT_ID +
                        "&client_secret=" + NAVER_CLIENT_SECRET + "&code=" + code,
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
        HttpURLConnection conn  = httpUtil.getConn(
                "https://openapi.naver.com/v1/nid/me",
                "GET"
        );

        conn.setRequestProperty("Authorization", tokenType + " " + accessToken);

        String str = httpUtil.getHttpResponse(conn);

        return ResponseEntity.ok(new SuccessResponse(str));
    }
}