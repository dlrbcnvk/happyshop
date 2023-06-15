package com.project.happyshop.security.authentication.provider;

import com.project.happyshop.domain.entity.Member;
import com.project.happyshop.security.util.JwtProperties;
import com.project.happyshop.service.MemberService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Service
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

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .setSubject(member.getEmail())
                .setClaims(claims)
                .signWith(keyPair.getPrivate(), SignatureAlgorithm.RS512)
                .compact();
    }

    // JWT 토큰 유효성 검증 메서드
    // 에러가 난다면 parserBuilder() 내부에서 에러가 터진 것임. 못 잡게 설계되어 있음.
    public boolean isValidToken(String token) {

        Jwts.parserBuilder()
                .setSigningKey(keyPair.getPublic())
                .build()
                .parseClaimsJws(token);

        return true;
    }

    // 토큰 기반으로 인증 정보를 가져오는 메서드
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);

        Set<GrantedAuthority> authorities = new HashSet<>();
        // TODO 권한 데이터를 어떻게 가져올 것인가가 문제임. 책에서는 ROLE_USER 를 수동으로 만드는데,
        //  나의 경우 이미 DB에 있음.

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
}
