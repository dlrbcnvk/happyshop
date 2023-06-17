package com.project.happyshop.service;

import com.project.happyshop.domain.Address;
import com.project.happyshop.domain.SocialProvider;
import com.project.happyshop.domain.entity.Member;
import com.project.happyshop.domain.entity.Role;
import com.project.happyshop.repository.JpaMemberRepository;
import com.project.happyshop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    private final JpaMemberRepository jpaMemberRepository;

    /**
     * 회원가입
     */
    @Transactional
    public Long join(Member member) throws IllegalStateException {
        // 중복 회원 여부 검사
        validateDuplicateMember(member);

        // 비밀번호 암호화
        if (member.getProvider() == SocialProvider.LOCAL) {
            member.encodePassword(passwordEncoder);
        }
        memberRepository.save(member);
        return member.getId();
    }


    /**
     * 전체 회원 조회
     */
    public List<Member> findMembers() { return memberRepository.findAll(); }

    /**
     * id 로 회원 조회
     */
    public Member findOne(Long id) { return memberRepository.findOne(id); }

    /**
     * (email, socialProvider) 로 회원조회
     */
    public Member findByEmailAndProvider(String email, SocialProvider socialProvider) {
        return jpaMemberRepository.findByEmailAndProvider(email, socialProvider);
    }

    /**
     * 회원 수정
     */
    public Member updateMember(Long id, String email, String password, String username, String phoneNumber, Address address) {
        Member findMember = memberRepository.findOne(id);
        // id 로 조회한 회원이 없으면 에러
        if (findMember == null) {
            throw new IllegalStateException("id 에 맞는 회원이 없습니다.");
        }
        // TODO 수정할 때도 이메일, 비밀번호, 전화번호 조건 따져야 함. 보충할 것

        return findMember.updateMember(email, password, username, phoneNumber, address);
    }

    /**
     * 중복 검사 메서드
     */
    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByEmailAndProvider(member.getEmail(), member.getProvider());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * 회원의 권한 조회
     */
    public Set<Role> getRoles(Member member) {
        // TODO
        return memberRepository.findRoles(member);
    }


}
