package com.project.happyshop.security.model;

import com.project.happyshop.domain.SocialProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;
import java.util.Map;

public interface ProviderUser {

    String getSocialId();
    String getUsername();
    String getPassword();
    String getEmail();
    String getProvider();
    SocialProvider getSocialProvider();
    List<? extends GrantedAuthority> getAuthorities();
    Map<String, Object> getAttributes();

    OAuth2User getOAuth2User();
}
