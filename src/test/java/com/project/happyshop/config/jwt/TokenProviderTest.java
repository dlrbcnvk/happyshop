package com.project.happyshop.config.jwt;

import com.nimbusds.jose.JOSEException;
import com.project.happyshop.domain.Address;
import com.project.happyshop.domain.SocialProvider;
import com.project.happyshop.domain.entity.Member;
import com.project.happyshop.repository.JpaMemberRepository;
import com.project.happyshop.security.authentication.provider.TokenProvider;
import com.project.happyshop.service.MemberService;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Rollback
@Slf4j
public class TokenProviderTest {

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private JpaMemberRepository jpaMemberRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private KeyPair keyPair;

    // generateToken() 테스트
    @DisplayName("generateToken(): 유저 정보와 만료 기간을 전달해 토큰을 만들 수 있다.")
    @Test
    void generateToken() {
        // given
        Member member = Member.createMember(
                "deapsea@sea.com",
                SocialProvider.LOCAL,
                "deapsea",
                "1234",
                "010-2313-1234",
                new Address("juso", "juso detail", "zipcode"));
        jpaMemberRepository.save(member);

        // when
        String token = tokenProvider.generateToken(member, Duration.ofDays(14));

        // then
        Long memberId = Jwts.parserBuilder()
                .setSigningKey(keyPair.getPublic())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("id", Long.class);

        assertThat(memberId).isEqualTo(tokenProvider.getMemberId(token));
    }

    // validToken() 검증 테스트
    @DisplayName("validToken(): 만료된 토큰인 때에 유효성 검증에 실패한다.")
    @Test
    void isValidToken_invalidToken() throws NoSuchAlgorithmException, JOSEException {
        // given
        String token = JwtFactory.builder()
                .expiration(new Date(new Date().getTime() - Duration.ofMinutes(1).toMillis()))
                .build()
                .createToken(tokenProvider);

        // when
        boolean result = tokenProvider.validToken(token);

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("validToken(): 유효한 토큰인 때에 유효성 검증에 성공한다.")
    @Test
    void validToken_validToken() throws NoSuchAlgorithmException, JOSEException {
        // given
        String token = JwtFactory.builder()
                .build()
                .createToken(tokenProvider);

        // when
        boolean result = tokenProvider.validToken(token);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("getAuthentication(): 토큰 기반으로 인증 정보를 가져올 수 있다.")
    @Test
    @Transactional
    void getAuthentication() {
        // given
        String email = "deapsea@sea.com";

        Member member = Member.createMember(
                email,
                SocialProvider.LOCAL,
                "deapsea",
                "1234",
                "010-2313-1234",
                new Address("juso", "juso detail", "zipcode"));
        memberService.join(member);

        String token = JwtFactory.builder()
                .subject(email)
                .claims(Map.of("id", member.getId()))
                .build()
                .createToken(tokenProvider);

        // when
        Authentication authentication = tokenProvider.getAuthentication(token);

        // result
        assertThat(((UserDetails) authentication.getPrincipal()).getUsername()).isEqualTo(email);
    }

    @DisplayName("getMemberId(): 토큰으로 회원 id 를 가져올 수 있다.")
    @Test
    void getMemberId() {
        // given
        Member member = Member.createMember(
                "deapsea@sea.com",
                SocialProvider.LOCAL,
                "deapsea",
                "1234",
                "010-2313-1234",
                new Address("juso", "juso detail", "zipcode"));
        jpaMemberRepository.save(member);

        String token = JwtFactory.builder()
                .claims(Map.of("id", member.getId()))
                .build()
                .createToken(tokenProvider);

        // when
        Long memberIdByToken = tokenProvider.getMemberId(token);

        // then
        assertThat(memberIdByToken).isEqualTo(member.getId());
    }
}
