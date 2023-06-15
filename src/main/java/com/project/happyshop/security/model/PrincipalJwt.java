package com.project.happyshop.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

// JWT 토큰 인증객체
@Data
public class PrincipalJwt implements Authentication {

    private Long id;
    private String jwtToken;
    private PrincipalUser principalUser;

    private boolean authenticated = false;

    public PrincipalJwt(Long id, String jwtToken, PrincipalUser principalUser) {
        this.id = id;
        this.jwtToken = jwtToken;
        this.principalUser = principalUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return principalUser.getAuthorities();
    }

    @Override
    public Object getCredentials() {
        return jwtToken;
    }

    @Override
    public Object getDetails() {
        return jwtToken;
    }

    @Override
    public Object getPrincipal() {
        return principalUser;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return principalUser.getName();
    }
}
