package com.project.happyshop.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.happyshop.domain.SocialProvider;
import com.project.happyshop.domain.dto.FormUserLoginDto;
import com.project.happyshop.domain.entity.Member;
import com.project.happyshop.exception.AuthMethodNotSupportedException;
import com.project.happyshop.security.authentication.service.CustomUserDetailsService;
import com.project.happyshop.security.model.PrincipalUser;
import com.project.happyshop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EmailPasswordApiAuthenticationFilter extends OncePerRequestFilter {

    private final String DEFAULT_API_LOGIN_URI = "/api/login";
    private final ObjectMapper objectMapper;
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    private final CustomUserDetailsService customUserDetailsService;

    @Override
    @Transactional
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!request.getRequestURI().equals(DEFAULT_API_LOGIN_URI)) {
            filterChain.doFilter(request, response);
        }

        if (!HttpMethod.POST.name().equals(request.getMethod())) {
            throw new AuthMethodNotSupportedException("Authentication method not supported");
        }

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        if (email == null || password == null) {
            throw new AuthenticationServiceException("Email or Password not provided");
        }
        FormUserLoginDto userDto = new FormUserLoginDto(email, password);
        Member findMember = memberService.findByEmailAndProvider(userDto.getEmail(), SocialProvider.LOCAL);

        // TODO 여기서 인코딩한 값이랑 db 에 인코딩 된 값이랑 다르네. decode 가 없어서 인코딩을 한 것인데 이러면 안 되나보다.
        //  decoding 할 방법이나 비밀번호를 맞출 수 있는 다른 방법을 생각해보자.
        if (!passwordEncoder.encode(userDto.getPassword()).equals(findMember.getPassword())) {
            throw new AuthenticationServiceException("Password not correct");
        }

        PrincipalUser principalUser = (PrincipalUser) customUserDetailsService.loadUserByUsername(findMember.getEmail());
        Set<SimpleGrantedAuthority> authorities = findMember.getMemberRoles().stream()
                .map(memberRole -> new SimpleGrantedAuthority(memberRole.getRole().getRoleName()))
                .collect(Collectors.toUnmodifiableSet());
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(principalUser, findMember.getPassword(), authorities);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
