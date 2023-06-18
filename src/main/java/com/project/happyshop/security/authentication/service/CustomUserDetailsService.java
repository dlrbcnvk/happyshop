package com.project.happyshop.security.authentication.service;

import com.project.happyshop.domain.entity.Member;
import com.project.happyshop.domain.SocialProvider;
import com.project.happyshop.exception.UsernameAndProviderNotFoundException;
import com.project.happyshop.repository.JpaMemberRepository;
import com.project.happyshop.repository.JpaMemberRoleRepository;
import com.project.happyshop.repository.JpaRoleRepository;
import com.project.happyshop.security.converters.ProviderUserRequest;
import com.project.happyshop.security.model.PrincipalUser;
import com.project.happyshop.security.model.ProviderUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service("userDetailsService")
public class CustomUserDetailsService extends AbstractOAuth2UserService implements UserDetailsService {

    @Autowired
    private JpaMemberRepository jpaMemberRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Member findMember = jpaMemberRepository.findByEmailAndProvider(email, SocialProvider.LOCAL);

        if (findMember == null) {
            throw new UsernameAndProviderNotFoundException("No member found with email and provider: {" + email + ", " + SocialProvider.LOCAL + "}");
        }

        ProviderUserRequest providerUserRequest = new ProviderUserRequest(findMember);
        ProviderUser providerUser = providerUser(providerUserRequest);
        return new PrincipalUser(providerUser);
    }
}
