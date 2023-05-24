package com.project.happyshop.security.service;

import com.project.happyshop.entity.Member;
import com.project.happyshop.entity.SocialProvider;
import com.project.happyshop.exception.EmailAndProviderNotFoundException;
import com.project.happyshop.repository.JpaMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service("userDetailsService")
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final JpaMemberRepository jpaMemberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member findMember = jpaMemberRepository.findByUsernameAndProvider(username, SocialProvider.LOCAL);

        if (findMember == null) {
            throw new EmailAndProviderNotFoundException("No member found with username and local provider: " + username);
        }


        return null;
    }
}
