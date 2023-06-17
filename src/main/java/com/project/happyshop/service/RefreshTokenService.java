package com.project.happyshop.service;

import com.project.happyshop.domain.entity.RefreshToken;
import com.project.happyshop.repository.JpaRefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final JpaRefreshTokenRepository jpaRefreshTokenRepository;

    public RefreshToken findByRefreshToken(String refreshToken) {
        return jpaRefreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected token"));
    }
}
