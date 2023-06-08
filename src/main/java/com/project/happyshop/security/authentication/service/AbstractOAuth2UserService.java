package com.project.happyshop.security.authentication.service;

import com.project.happyshop.domain.SocialProvider;
import com.project.happyshop.domain.entity.Member;
import com.project.happyshop.security.converters.ProviderUserConverter;
import com.project.happyshop.security.converters.ProviderUserRequest;
import com.project.happyshop.security.model.ProviderUser;
import com.project.happyshop.security.repository.InMemoryTemporarySocialRepository;
import com.project.happyshop.security.repository.InMemoryTemporarySocialRepository.SocialUser;
import com.project.happyshop.service.MemberService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Getter
public abstract class AbstractOAuth2UserService {

    @Autowired
    private ProviderUserConverter<ProviderUserRequest, ProviderUser> providerUserConverter;

    @Autowired
    private MemberService memberService;

    @Autowired
    private InMemoryTemporarySocialRepository temporarySocialRepository;

    protected ProviderUser providerUser(ProviderUserRequest providerUserRequest) {
        return providerUserConverter.converter(providerUserRequest);
    }

    protected Member findByEmailAndProvider(String email, SocialProvider socialProvider) {
        return memberService.findByEmailAndProvider(email, socialProvider);
    }

    protected SocialUser findBySocialId(String socialId) {
        return temporarySocialRepository.findBySocialId(socialId);
    }

    protected void register(ProviderUser providerUser) {
        temporarySocialRepository.register(providerUser);
    }

    protected void delete(String socialId) {
        temporarySocialRepository.delete(socialId);
    }
}
