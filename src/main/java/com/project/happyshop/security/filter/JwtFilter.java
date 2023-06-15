package com.project.happyshop.security.filter;

import com.project.happyshop.domain.SocialProvider;
import com.project.happyshop.domain.entity.Member;
import com.project.happyshop.repository.JpaMemberRepository;
import com.project.happyshop.security.authentication.provider.TokenProvider;
import com.project.happyshop.security.model.PrincipalJwt;
import com.project.happyshop.security.model.PrincipalUser;
import com.project.happyshop.security.repository.InMemoryJwtTokenRepository;
import com.project.happyshop.security.token.JwtAuthenticationToken;
import com.project.happyshop.security.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.KeyPair;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String DEFAULT_FILTER_PROCESSES_URI = "/api/";


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 요청 헤더의 Authorization 키 값 조회
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);

        // 가져온 값에서 접두사 제거
        String token = getAccessToken(authorizationHeader);

//        // 가져온 토큰이 유효한지 확인하고, 유효한 경우 인증 객체 저장
//        if (tokenProvider.validToken(token)) {
//            Authentication authentication = tokenProvider.getAuthentication(token);
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//        }

        filterChain.doFilter(request, response);

        String uri = request.getRequestURI();
        if (!uri.startsWith(DEFAULT_FILTER_PROCESSES_URI)) {
        }
    }

    protected String getAccessToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
            return authorizationHeader.substring(TOKEN_PREFIX.length());
        }
        return null;
    }
}
