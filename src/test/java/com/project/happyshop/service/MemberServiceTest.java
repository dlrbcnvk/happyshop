package com.project.happyshop.service;

import com.project.happyshop.entity.Address;
import com.project.happyshop.entity.Member;
import com.project.happyshop.entity.SocialProvider;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Test
    public void 회원가입() {

        Member member = createMember1();

        Long savedId = memberService.join(member);

        assertEquals(member, memberService.findOne(savedId));
    }

    @Test
    public void 중복_회원_예외() {
        Member member1 = createMember1();
        Member member2 = createMember1();
        memberService.join(member1);

        assertThrows(IllegalStateException.class, () -> memberService.join(member2));
    }

    @Test
    public void 회원단건조회_없음() { // id 로 단건 조회 시 null
        Member member = createMember1();
        memberService.join(member);
        Member findMember = memberService.findOne(Long.MAX_VALUE);
        Assertions.assertThat(findMember).isEqualTo(null);
    }

    @Test
    public void 회원가입_실패_이메일() {
        Member member = Member.createMember(
                "dlrbcnvkgmail.com",
                SocialProvider.LOCAL,
                "34254325~",
                "sdsad",
                "010-3333-1234",
                new Address("경기도", "어느 집", "12345")
        );

        assertThrows(IllegalStateException.class, () -> memberService.join(member));
    }

    @Test
    public void 회원가입_실패_비밀번호_특수문자없음() {
        Member member = Member.createMember(
                "dlrbcnvk@gmail.com",
                SocialProvider.LOCAL,
                "34254325",
                "sdsad",
                "010-3333-1234",
                new Address("경기도", "어느 집", "12345")
        );

        assertThrows(IllegalStateException.class, () -> memberService.join(member));
    }

    @Test
    public void 회원가입_실패_비밀번호_8자리미만() {
        Member member = Member.createMember(
                "dlrbcnvk@gmail.com",
                SocialProvider.LOCAL,
                "342543",
                "sdsad",
                "010-3333-1234",
                new Address("경기도", "어느 집", "12345")
        );

        assertThrows(IllegalStateException.class, () -> memberService.join(member));
    }

    @Test
    public void 회원가입_실패_비밀번호_연속된3자리숫자() {
        Member member = Member.createMember(
                "dlrbcnvk@gmail.com",
                SocialProvider.LOCAL,
                "dksjfksaljfklsef1234asdljfks",
                "sdsad",
                "010-3333-1234",
                new Address("경기도", "어느 집", "12345")
        );

        assertThrows(IllegalStateException.class, () -> memberService.join(member));
    }

    @Test
    public void 회원가입_실패_전화번호_올바른형식이아님() {
        Member member = Member.createMember(
                "dlrbcnvk@gmail.com",
                SocialProvider.LOCAL,
                "34254325~",
                "sdsad",
                "01033331234",
                new Address("경기도", "어느 집", "12345")
        );

        assertThrows(IllegalStateException.class, () -> memberService.join(member));
    }

    @Test
    public void 회원가입_실패_전화번호_국내에존재하지않는지역번호() {
        Member member = Member.createMember(
                "dlrbcnvk@gmail.com",
                SocialProvider.LOCAL,
                "34254325~",
                "sdsad",
                "22-1234-1234",
                new Address("경기도", "어느 집", "12345")
        );

        assertThrows(IllegalStateException.class, () -> memberService.join(member));
    }

    private Member createMember1() {
        Member member = Member.createMember(
                "dlrbcnvk@gmail.com",
                SocialProvider.LOCAL,
                "34254325~",
                "sdsad",
                "010-1234-1234",
                new Address("경기도", "어느 집", "12345")
        );
        return member;
    }

    private Member createMember2() {
        Member member = Member.createMember(
                "dlrbcnvk@naver.com",
                SocialProvider.NAVER,
                "12341234",
                "이자시",
                "010-4321-4321",
                new Address("경기도", "어느 집", "12345")
        );
        return member;
    }
}
