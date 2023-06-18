package com.project.happyshop.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.happyshop.domain.SocialProvider;
import com.project.happyshop.domain.dto.FormUserLoginDto;
import com.project.happyshop.domain.entity.Member;
import com.project.happyshop.domain.entity.Role;
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
import org.springframework.security.core.GrantedAuthority;
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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EmailPasswordApiAuthenticationFilter extends OncePerRequestFilter {

    private final String DEFAULT_API_LOGIN_URI = "/api/login";
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    private final CustomUserDetailsService customUserDetailsService;

    @Override
    @Transactional(readOnly = true)
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!request.getRequestURI().equals(DEFAULT_API_LOGIN_URI)) {
            filterChain.doFilter(request, response);
            return;
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

        if (!passwordEncoder.matches(password, findMember.getPassword())) {
            throw new AuthenticationServiceException("Password not correct");
        }

        Set<Role> roles = memberService.getRoles(findMember);

        PrincipalUser principalUser = (PrincipalUser) customUserDetailsService.loadUserByUsername(findMember.getEmail());
        List<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toList());
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(principalUser, findMember.getPassword(), authorities);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
