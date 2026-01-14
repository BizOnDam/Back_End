package com.bizondam.contract_service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Slf4j
public class CustomUserHeaderAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String userId = request.getHeader("X-User-Id");
        String userRole = request.getHeader("X-User-Role");
        log.info("받은 사용자 ID: {}", userId);

        if (userId != null && userRole != null) {
            log.debug("[인증 필터] userId={}, userRole={}", userId, userRole);
            // ROLE 접두어 붙이기
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + userRole.toUpperCase());
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userId, null, Collections.singletonList(authority));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            log.debug("[인증 필터] 헤더 없음 -> 익명 사용자");
        }

        filterChain.doFilter(request, response);
    }
}
