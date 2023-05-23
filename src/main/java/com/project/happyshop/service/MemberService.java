package com.project.happyshop.service;

import com.project.happyshop.domain.Address;
import com.project.happyshop.domain.Member;
import com.project.happyshop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원가입
     */
    @Transactional
    public Long join(Member member) throws IllegalStateException {
        // 중복 회원 여부 검사
        validateDuplicateMember(member);
        // 이메일, 비밀번호, 전화번호 형식이 맞는지 검사
        validateEmailPasswordPhoneFormat(member);
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
     * 회원 수정
     */
    public Long updateMember(Long id, String email, String password, String username, String phoneNumber, Address address) {
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

    private void validateEmailPasswordPhoneFormat(Member member) {
        validateEmailFormat(member);
        validatePasswordFormat(member);
        validatePhoneFormat(member);
    }

    private void validateEmailFormat(Member member) {
        /**
         * 이메일 조건: (영어,숫자)@영어.영어
         */
        String email = member.getEmail();
        Pattern emailPattern = Pattern.compile("[a-zA-Z1-9]+@[a-zA-Z]+.[a-zA-Z]+");
        if (!emailPattern.matcher(email).matches()) {
            throw new IllegalStateException("이메일은 (영어 또는 숫자)@영어.영어 형식이어야 합니다.");
        }
    }

    private void validatePasswordFormat(Member member) {
        /**
         * 비밀번호 조건: 8자리 이상, 연속된 3자리 숫자 안됨, 특수기호 ~!@#$%^&*()_+ 중 하나 이상 포함할 것
         */
        String password = member.getPassword();
        if (password.length() < 8) {
            throw new IllegalStateException("비밀번호는 8자리 이상이어야 합니다.");
        }
        Pattern passwordSpecialPattern = Pattern.compile(".*[~!@#$%^&*()_+]+.*");
        if (!passwordSpecialPattern.matcher(password).matches()) {
            throw new IllegalStateException("비밀번호에는 특수기호 ~!@#$%^&*()_+ 중 하나 이상 포함하여야 합니다.");
        }

        Pattern passwordContinuousPattern = Pattern.compile(".*(012|123|234|345|456|567|678|789|890).*");
        if (passwordContinuousPattern.matcher(password).matches()) {
            throw new IllegalStateException("비밀번호에 연속된 3자리 숫자는 사용할 수 없습니다.");
        }
    }

    private void validatePhoneFormat(Member member) {
        /**
         * 전화번호는 국내 번호에 한정됨.
         * 전화번호 조건: (2~3자리)-(3-4자리)-(4자리)
         * 첫 2~3자리는 지역 번호, 010, 011 중 하나여야 함.
         * 지역번호
         *   서울: 02, 부산: 051, 대구: 053, 인천: 032, 광주: 062, 대전: 042, 울산 052,
         *   세종: 044, 경기: 031, 강원: 033, 충북: 043, 충남: 041, 전북: 063, 전남: 061,
         *   경북: 054, 경남: 055, 제주: 064
         */
        String phoneNumber = member.getPhoneNumber();
        Pattern phoneNumberPattern = Pattern.compile("[0-9]{2,3}-[0-9]{3,4}-[0-9]{3,4}");
        if (!phoneNumberPattern.matcher(phoneNumber).matches()) {
            throw new IllegalStateException("전화번호는 (2~3자리 숫자)-(3~4자리 숫자)-(3~4자리 숫자) 형식이어야 합니다.");
        }

        String[] phoneFirstList = new String[]{
                "010", "011", "02", "051", "053", "032", "062", "042", "052",
                "044", "031", "033", "043", "041", "063", "061", "054", "055", "064"};

        String phoneFirst = phoneNumber.split("-")[0];
        boolean phoneFirstMatcher = false;
        for (String first : phoneFirstList) {
            if (phoneFirst.equals(first)) {
                phoneFirstMatcher = true;
            }
        }
        if (!phoneFirstMatcher) {
            throw new IllegalStateException("존재하지 않는 휴대전화번호 또는 지역번호입니다.");
        }
    }
}
