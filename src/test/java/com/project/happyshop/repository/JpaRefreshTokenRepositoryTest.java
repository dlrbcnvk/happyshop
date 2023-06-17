package com.project.happyshop.repository;

import com.project.happyshop.domain.entity.RefreshToken;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@SpringBootTest
@Rollback
@Transactional
public class JpaRefreshTokenRepositoryTest {

    @Autowired
    private JpaRefreshTokenRepository refreshTokenRepository;

    @DisplayName("save(): 리프레시 토큰 저장하기")
    @Test
    void save() {
        // given
        Long memberId = 333L;
        String token = "askdfjlkaesjfkelaswjf";
        RefreshToken refreshToken = new RefreshToken(memberId, token);
        refreshTokenRepository.save(refreshToken);

        // when
        Optional<RefreshToken> findRefreshToken = refreshTokenRepository.findByMemberId(memberId);
        Assertions.assertThat(findRefreshToken.orElseThrow().getRefreshToken()).isEqualTo(token);
    }

}
