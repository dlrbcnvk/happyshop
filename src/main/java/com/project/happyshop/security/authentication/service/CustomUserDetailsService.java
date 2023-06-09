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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service("userDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailsService extends AbstractOAuth2UserService implements UserDetailsService {

    private final JpaMemberRepository jpaMemberRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member findMember = jpaMemberRepository.findByUsernameAndProvider(username, SocialProvider.LOCAL);

        if (findMember == null) {
            throw new UsernameAndProviderNotFoundException("No member found with username and provider: {" + username + ", " + SocialProvider.LOCAL + "}");
        }

        ProviderUserRequest providerUserRequest = new ProviderUserRequest(findMember);
        ProviderUser providerUser = providerUser(providerUserRequest);
        return new PrincipalUser(providerUser);
    }
}
