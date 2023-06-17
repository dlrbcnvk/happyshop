package com.project.happyshop.security.authentication.provider;

import com.project.happyshop.domain.entity.Member;
import com.project.happyshop.domain.entity.Role;
import com.project.happyshop.security.util.JwtProperties;
import com.project.happyshop.service.MemberService;
import io.jsonwebtoken.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.KeyPair;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
@Getter
public class TokenProvider {

    private final JwtProperties jwtProperties;
    private final KeyPair keyPair;
    private final MemberService memberService;

    public String generateToken(Member member, Duration expiredAt) {
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), member);
    }

    // JWT 토큰 생성 메서드
    private String makeToken(Date expiry, Member member) {
        Date now = new Date();

        Map<String, Object> claims = new ConcurrentHashMap<>();
        claims.put("id", member.getId());
        claims.put("provider", member.getProvider().toString());
        claims.put("username", member.getUsername());

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .setSubject(member.getEmail())
                .setClaims(claims)
                .signWith(keyPair.getPrivate(), SignatureAlgorithm.RS512)
                .compact();
    }

    // 토큰 기반으로 인증 정보를 가져오는 메서드
    @Transactional
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);


        // TODO 권한 데이터를 어떻게 가져올 것인가가 문제임. 책에서는 ROLE_USER 를 수동으로 만드는데,
        //  나의 경우 이미 DB에 있음.
        //  member, member_role, role 테이블의 관계, 개선 포인트가 있을지 생각해볼 것
        Long memberIdByToken = getMemberId(token);
        Member findMember = memberService.findOne(memberIdByToken);
        Set<Role> roles = memberService.getRoles(findMember);
        Set<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toUnmodifiableSet());

        return new UsernamePasswordAuthenticationToken(new User(claims.getSubject(), "", authorities), authorities);
    }

    // 토큰 기반으로 회원 ID를 가져오는 메서드
    public Long getMemberId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(keyPair.getPublic())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    // JWT 토큰 유효성 검증 메서드
    public boolean validToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(keyPair.getPublic())
                    .build()
                    .parseClaimsJws(token);

        } catch (JwtException e) { // 복호화 과정에서 예외가 발생하면 유효하지 않은 토큰
            log.info("JwtException is occurred");
            return false;
        }
        return true;
    }
}
