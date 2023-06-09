package com.project.happyshop.security.converters;

import com.project.happyshop.domain.entity.Member;
import com.project.happyshop.repository.JpaMemberRepository;
import com.project.happyshop.security.model.ProviderUser;
import com.project.happyshop.security.model.local.FormUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;


public class UserDetailsProviderUserConverter implements ProviderUserConverter<ProviderUserRequest, ProviderUser> {

    @Autowired
    private JpaMemberRepository jpaMemberRepository;

    @Override
    @Transactional
    public ProviderUser converter(ProviderUserRequest providerUserRequest) {
        if (providerUserRequest.member() == null) {
            return null;
        }

        Member member = providerUserRequest.member();
        Member findMember = jpaMemberRepository.findById(member.getId()).orElseThrow();

        return FormUser.builder()
                .id(findMember.getId() + "")
                .username(findMember.getUsername())
                .password(findMember.getPassword())
                .email(findMember.getEmail())
                .authorities(findMember.getMemberRoles().stream().map(memberRole ->
                    new SimpleGrantedAuthority(memberRole.getRole().getRoleName())).collect(Collectors.toList()))
                .provider(findMember.getProvider().toString())
                .build();
    }
}
