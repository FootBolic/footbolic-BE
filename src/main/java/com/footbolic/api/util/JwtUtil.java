package com.footbolic.api.util;

import com.footbolic.api.member.dto.MemberDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";

    private static final String BEARER_TYPE = "Bearer";

    private final String ID_CLAIM = "id";

    private final String AUTH_CLAIM = "auth";

    private final String PLATFORM_CLAIM = "platform";

    private final String ID_AT_PROVIDER_CLAIM = "idAtProvider";

    private static final Long REFRESH_TOKEN_EXPIRES_IN = 180 * 24 * 60 * 60 * 1000L;

    private static final Long ACCESS_TOKEN_EXPIRES_IN =   30 * 60 * 1000L;

    private final Key key;

    public JwtUtil(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 현재시간 기준 Refresh Token의 만료시간을 반환한다
     * @return Refresh Token 만료시간
     */
    public Date getRefreshExpiration() {
        return new Date(new Date().getTime() + REFRESH_TOKEN_EXPIRES_IN);
    }

    /**
     * 현재시간 기준 Access Token의 만료시간을 반환한다
     * @return Access Token 만료시간
     */
    public Date getAccessExpiration() {
        return new Date(new Date().getTime() + ACCESS_TOKEN_EXPIRES_IN);
    }

    /**
     * 회원 식별번호로 RefreshToken 생성
     */
    public String generateRefreshToken(MemberDto member, Date expiration) {
        return Jwts.builder()
                .claim(ID_CLAIM, member.getId())
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 회원정보로 AccessToken 생성
     */
    public String generateAccessToken(MemberDto member, Date expiration) {
        return Jwts.builder()
                .setSubject(member.getNickname())
                .claim(ID_CLAIM, member.getId())
                .claim(AUTH_CLAIM, member.getRoleId())
                .claim(PLATFORM_CLAIM, member.getPlatform())
                .claim(ID_AT_PROVIDER_CLAIM, member.getIdAtProvider())
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Access Token에 들어있는 정보 복호화
     */
    public MemberDto resolveAccessToken(String accessToken) {

        Claims claims = parseClaims(accessToken);

        if (claims.get(AUTH_CLAIM) == null) {
            log.error("권한정보가 없는 토큰입니다.");
            return null;
        }

        return MemberDto.builder()
                .id(claims.get(ID_CLAIM).toString())
                .platform(claims.get(PLATFORM_CLAIM).toString())
                .idAtProvider(claims.get(ID_AT_PROVIDER_CLAIM).toString())
                .roleId(claims.get(AUTH_CLAIM).toString())
                .accessTokenExpiresAt(LocalDateTime.ofInstant(claims.getExpiration().toInstant(), ZoneId.systemDefault()))
                .build();
    }

    /**
     * Refresh Token에 들어있는 정보 복호화
     */
    public MemberDto resolveRefreshToken(String refreshToken) {

        Claims claims = parseClaims(refreshToken);

        return MemberDto.builder()
                .id(claims.get(ID_CLAIM).toString())
                .refreshTokenExpiresAt(LocalDateTime.ofInstant(claims.getExpiration().toInstant(), ZoneId.systemDefault()))
                .build();
    }

    /**
     * 토큰 정보를 검증하는 메서드
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty.", e);
        }
        return false;
    }

    /**
     * 토큰에서 body에 실린 데이터 추출
     */
    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    /**
     * 헤더에서 액세스 토큰 추출
     */
    public String extractAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_TYPE)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * 쿠키에서 Refresh Token 을 추출한다.
     * @param request HttpServletRequest 객체
     * @return 쿠키에서 추출한 Refresh Token
     */
    public String extractRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName() != null && cookie.getName().equals(REFRESH_TOKEN_COOKIE_NAME))
                    return cookie.getValue();
            }
        }

        return null;
    }

    /**
     * Date 객체를 LocalDateTime 객체로 변환한다.
     * @param date 변환할 Date 객체
     * @return 변환된 LocalDateTime 객체
     */
    public LocalDateTime toLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * 쿠키에 Refresh Token을 발급한다
     * @param response HttpServletResponse 객체
     * @param refreshToken 발급할 Refresh Tokens
     */
    public void issueRefreshToken(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie("refresh_token", refreshToken);

        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge((180 * 24 * 60 * 60) - (60 * 60));
        cookie.setPath("/");
        cookie.setDomain("localhost");
        cookie.setAttribute("SameSite", "none");

        response.addCookie(cookie);
    }

    /**
     * 쿠키에 저장된 Refresh Token을 제거한다
     * @param response HttpServletResponse 객체
     */
    public void removeRefreshToken(HttpServletResponse response) {
        Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, "");

        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addCookie(cookie);
    }

}
