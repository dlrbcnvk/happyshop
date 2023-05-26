package com.project.happyshop.security.authentication.service;

import com.project.happyshop.domain.entity.Member;
import com.project.happyshop.domain.SocialProvider;
import com.project.happyshop.exception.UsernameAndProviderNotFoundException;
import com.project.happyshop.repository.JpaMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service("userDetailsService")
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final JpaMemberRepository jpaMemberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member findMember = jpaMemberRepository.findByUsernameAndProvider(username, SocialProvider.LOCAL);

        if (findMember == null) {
            throw new UsernameAndProviderNotFoundException("No member found with username and provider: {" + username + ", " + SocialProvider.LOCAL + "}");
        }

        Set<String> userRoles = findMember.getMemberRoles()
                .stream()
                .map(memberRole -> memberRole.getRole().getRoleName())
                .collect(Collectors.toSet());

        return new UserDetail(findMember, new ArrayList<>(userRoles));
    }
}
