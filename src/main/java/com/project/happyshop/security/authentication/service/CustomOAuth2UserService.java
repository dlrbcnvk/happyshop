package com.project.happyshop.security.authentication.service;

import com.project.happyshop.domain.entity.Member;
import com.project.happyshop.security.converters.ProviderUserRequest;
import com.project.happyshop.security.model.PrincipalUser;
import com.project.happyshop.security.model.ProviderUser;
import com.project.happyshop.security.repository.InMemoryTemporarySocialRepository;
import com.project.happyshop.security.repository.InMemoryTemporarySocialRepository.SocialUser;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends AbstractOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        ProviderUserRequest providerUserRequest = new ProviderUserRequest(clientRegistration, oAuth2User);

        // google, naver, kakao 중 어느것인지 판단
        ProviderUser providerUser = super.providerUser(providerUserRequest);

        // 회원가입 or 이미 있으면 pass
        Member findMember = super.findByEmailAndProvider(providerUser.getEmail(), providerUser.getSocialProvider());
        if (findMember == null) {
            // 아직 회원이 아님.
            SocialUser socialUser = super.findBySocialId(providerUser.getSocialId());
            if (socialUser == null) {
                // 임시로도 저장 안 되있음. 첫번째 방문
                // 임시 db 에 일단 넣어야 함.
                super.register(providerUser);
            } else {
                // 소셜로그인 인증은 했는데, 사이트 회원가입은 아직 하지 않은 상황

            }
        }

        return new PrincipalUser(providerUser);
    }
}
