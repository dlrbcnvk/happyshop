package com.project.happyshop.security.converters;

import com.project.happyshop.security.model.ProviderUser;
import com.project.happyshop.security.model.social.NaverUser;
import com.project.happyshop.security.util.OAuth2Utils;

public class OAuth2NaverProviderUserConverter implements ProviderUserConverter<ProviderUserRequest, ProviderUser> {
    @Override
    public ProviderUser converter(ProviderUserRequest providerUserRequest) {

        // TODO "naver" 과 같은 social name 들을 enum 타입으로 모아서 관리할 것
        if (!providerUserRequest.clientRegistration().getRegistrationId().equals("naver")) {
            return null;
        }
        return new NaverUser(OAuth2Utils.getSubAttributes(providerUserRequest.oAuth2User(), "response"),
                providerUserRequest.oAuth2User(),
                providerUserRequest.clientRegistration());
    }
}
