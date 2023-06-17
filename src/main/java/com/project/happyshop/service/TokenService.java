package com.project.happyshop.service;

import com.project.happyshop.domain.entity.Member;
import com.project.happyshop.security.authentication.provider.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final MemberService memberService;

    public String createNewAccessToken(String refreshToken) {
        // 토큰 유효성 검사에 실패하면 예외 발생
        if (!tokenProvider.validToken(refreshToken)) {
            throw new IllegalArgumentException("Unexpected token");
        }

        Long findMemberId = refreshTokenService.findByRefreshToken(refreshToken).getMemberId();
        Member findMember = memberService.findOne(findMemberId);

        return tokenProvider.generateToken(findMember, Duration.ofHours(1));
    }
}
