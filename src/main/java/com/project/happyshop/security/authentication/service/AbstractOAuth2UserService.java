package com.project.happyshop.security.authentication.service;

import com.project.happyshop.domain.SocialProvider;
import com.project.happyshop.domain.entity.Member;
import com.project.happyshop.security.converters.ProviderUserConverter;
import com.project.happyshop.security.converters.ProviderUserRequest;
import com.project.happyshop.security.model.ProviderUser;
import com.project.happyshop.security.repository.InMemoryTemporarySocialRepository;
import com.project.happyshop.security.repository.InMemoryTemporarySocialRepository.SocialUser;
import com.project.happyshop.service.MemberService;
import io.jsonwebtoken.Jwts;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@Getter
public abstract class AbstractOAuth2UserService {

    @Autowired
    private ProviderUserConverter<ProviderUserRequest, ProviderUser> providerUserConverter;

    @Autowired
    private MemberService memberService;

    @Autowired
    private InMemoryTemporarySocialRepository temporarySocialRepository;

    @Autowired
    private KeyPair keyPair;

    protected ProviderUser providerUser(ProviderUserRequest providerUserRequest) {
        return providerUserConverter.converter(providerUserRequest);
    }

    protected Member findByEmailAndProvider(String email, SocialProvider socialProvider) {
        return memberService.findByEmailAndProvider(email, socialProvider);
    }

    protected SocialUser findBySocialId(String socialId) {
        return temporarySocialRepository.findBySocialId(socialId);
    }

    protected void register(ProviderUser providerUser) {
        temporarySocialRepository.register(providerUser);
    }

    protected void delete(String socialId) {
        temporarySocialRepository.delete(socialId);
    }

    protected String setJwtToken() {

        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("typ", "JWT");
        headerMap.put("alg", "RS512");

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", "id from request");
        claims.put("name", "name from request");

        // 만료시간 설정 (millisecond 단위. 1시간으로 설정)
        Date expireTime = new Date();
        long expireTimeLong = expireTime.getTime() + 1000 * 60 * 60 * 1;
        expireTime.setTime(expireTimeLong);

        String jwt = Jwts.builder()
                .setHeader(headerMap)
                .setClaims(claims)
                .signWith(keyPair.getPrivate())
                .compact();
        log.info("jwt={}", jwt);

        return jwt;
    }
}
