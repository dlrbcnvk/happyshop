package com.project.happyshop.security.converters;

import com.project.happyshop.domain.entity.Member;
import com.project.happyshop.repository.JpaMemberRepository;
import com.project.happyshop.security.model.ProviderUser;
import com.project.happyshop.security.model.local.FormUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@RequiredArgsConstructor
public class UserDetailsProviderUserConverter implements ProviderUserConverter<ProviderUserRequest, ProviderUser> {

    @Override
    @Transactional
    public ProviderUser converter(ProviderUserRequest providerUserRequest) {
        if (providerUserRequest.member() == null) {
            return null;
        }

        Member member = providerUserRequest.member();

        return FormUser.builder()
                .id(member.getId() + "")
                .username(member.getUsername())
                .password(member.getPassword())
                .email(member.getEmail())
                .authorities(member.getMemberRoles().stream().map(memberRole ->
                    new SimpleGrantedAuthority(memberRole.getRole().getRoleName())).collect(Collectors.toList()))
                .provider(member.getProvider().toString())
                .build();
    }
}
