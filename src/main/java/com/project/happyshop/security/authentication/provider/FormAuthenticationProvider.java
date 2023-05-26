package com.project.happyshop.security.authentication.provider;

import com.project.happyshop.security.authentication.service.UserDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class FormAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        String loginId = auth.getName();
        String password = (String) auth.getCredentials();

        UserDetails userDetails = null;

        try {
            // 사용자 조회
            userDetails = userDetailsService.loadUserByUsername(loginId);

            if (userDetails == null || !passwordEncoder.matches(password, userDetails.getPassword())) {
                throw new BadCredentialsException("Invalid password");
            }

            if (!userDetails.isEnabled()) {
                throw new BadCredentialsException("not user confirm");
            }
        } catch (UsernameNotFoundException e) {
            log.info(e.toString());
            throw new UsernameNotFoundException(e.getMessage());
        } catch (BadCredentialsException e) {
            log.info(e.toString());
            throw new BadCredentialsException(e.getMessage());
        } catch (Exception e) {
            log.info(e.toString());
            throw new RuntimeException(e.getMessage());
        }

        return new UsernamePasswordAuthenticationToken(
                ((UserDetail) userDetails).getMember(),
                null,
                userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
