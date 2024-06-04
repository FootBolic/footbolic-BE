package com.footbolic.api.aop;

import com.footbolic.api.annotation.RoleCheck;
import com.footbolic.api.annotation.RoleCode;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.List;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuthAspect {

    @Pointcut("@annotation(com.footbolic.api.annotation.RoleCheck)")
    public void onRoleCheck(){}

    /**
     * 사용자가 요구되는 역할을 가지고 있는지 확인한다,
     * @param joinPoint AOP JoinPoint 객체
     * @return AOP JoinPoint proceed 객체
     * @throws Throwable 에러
     */
    @Around("onRoleCheck()")
    public Object checkRole(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        RoleCheck roleCheck = methodSignature.getMethod().getAnnotation(RoleCheck.class);
        List<String> requiredCodes = new java.util.ArrayList<>(Arrays.stream(roleCheck.codes()).map(RoleCode::code).toList());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        List<String> userCodes = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        requiredCodes.add(RoleCode.ROLE_SYS_MNG);

        for (String s : requiredCodes) {
            if (userCodes.contains(s)) return joinPoint.proceed();
        }

        HttpServletResponse response =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();

        if (response != null) response.sendError(401, "권한이 없는 사용자입니다.");

        return null;
    }
}