package com.project.happyshop.security.token;

import com.project.happyshop.security.model.PrincipalJwt;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {


    /**
     * Creates a token with the supplied array of authorities.
     *
     * @param authorities the collection of <tt>GrantedAuthority</tt>s for the principal
     *                    represented by this authentication object.
     */
    public JwtAuthenticationToken(Collection<? extends GrantedAuthority> authorities, PrincipalJwt principalJwt) {
        super(authorities);
        this.setDetails(principalJwt);
    }

    @Override
    public Object getCredentials() {
        return getPrincipal();
    }

    @Override
    public Object getPrincipal() {
        return getDetails();
    }
}
