package com.project.happyshop.controller;

import com.project.happyshop.domain.Address;
import com.project.happyshop.domain.SocialProvider;
import com.project.happyshop.domain.dto.SocialUserDto;
import com.project.happyshop.domain.dto.UserDto;
import com.project.happyshop.domain.entity.Member;
import com.project.happyshop.domain.entity.MemberRole;
import com.project.happyshop.domain.entity.Role;
import com.project.happyshop.security.model.PrincipalUser;
import com.project.happyshop.service.MemberRoleService;
import com.project.happyshop.service.MemberService;
import com.project.happyshop.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.regex.Pattern;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final RoleService roleService;
    private final MemberRoleService memberRoleService;

    @GetMapping("/register")
    public String register(@ModelAttribute UserDto userDto) {
        return "/member/login/register";
    }

    @GetMapping("/register/social/{email}/{username}")
    public String register(
            @PathVariable("email") String email,
            @PathVariable("username") String username,
            Model model) {
        model.addAttribute("email", email);
        model.addAttribute("username", username);
        return "/member/login/registerSocial";
    }

    @PostMapping("/register")
    @Transactional
    public String register(@Validated @ModelAttribute UserDto userDto, BindingResult bindingResult) {

        // 검증 로직
        // 이메일, 비밀번호, 전화번호 형식이 맞는지 검사
        validateEmailPasswordPhoneFormat(userDto, bindingResult);

        // 검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            return "/member/login/register";
        }

        // 성공 로직

        Member member = Member.createMember(
                userDto.getEmail(),
                SocialProvider.LOCAL,
                userDto.getUsername(),
                userDto.getPassword(),
                userDto.getPhoneNumber1() + "-" + userDto.getPhoneNumber2() + "-" + userDto.getPhoneNumber3(),
                new Address(userDto.getJuso(), userDto.getJusoDetail(), userDto.getZipcode())
        );

        try {
            memberService.join(member);
            Role role_user = roleService.findByRoleName("ROLE_USER");
            MemberRole memberRole = new MemberRole();
            memberRole.setMemberAndRole(member, role_user);
            memberRoleService.save(memberRole);
        } catch (RuntimeException e) {
            log.info(e.getMessage());
            return "redirect:/register";
        }

        return "redirect:/successRegister";
    }

    @PostMapping("/register/social")
    @Transactional
    public String register(SocialUserDto socialUserDto, @AuthenticationPrincipal PrincipalUser principalUser) {
        if (principalUser == null || principalUser.getSocialProvider() == SocialProvider.LOCAL) {
            return "redirect:/";
        }

        log.info(socialUserDto.toString());
        Member member = Member.createSocialMember(
                socialUserDto.getEmail(),
                principalUser.getSocialProvider(),
                socialUserDto.getUsername(),
                socialUserDto.getPhoneNumber1() + "-" + socialUserDto.getPhoneNumber2() + "-" + socialUserDto.getPhoneNumber3(),
                new Address(socialUserDto.getJuso(), socialUserDto.getJusoDetail(), socialUserDto.getZipcode()),
                principalUser.getSocialId()
        );

        try {
            memberService.join(member);
            Role role_user = roleService.findByRoleName("ROLE_USER");
            MemberRole memberRole = new MemberRole();
            memberRole.setMemberAndRole(member, role_user);
            memberRoleService.save(memberRole);
        } catch (RuntimeException e) {
            log.info(e.getMessage());
            return "redirect:/register/social";
        }

        return "redirect:/successRegister";
    }

    @GetMapping("/successRegister")
    public String successRegister() {
        return "/member/login/successRegister";
    }



    private void validateEmailPasswordPhoneFormat(UserDto userDto, BindingResult bindingResult) {
        validateEmailFormat(userDto, bindingResult);
        validatePasswordFormat(userDto, bindingResult);
        validatePhoneFormat(userDto, bindingResult);
    }

    private void validateEmailFormat(UserDto userDto, BindingResult bindingResult) {
        /**
         * 이메일 조건: (영어,숫자)@영어.영어
         */
        String email = userDto.getEmail();
        Pattern emailPattern = Pattern.compile("[a-zA-Z1-9.]+@[a-zA-Z]+(.[a-zA-Z]+)+");
        if (!emailPattern.matcher(email).matches()) {
            bindingResult.addError(new FieldError("userDto", "email", userDto.getEmail() + " 거절당함", false, new String[]{"email.pattern"}, null, null));
        }
    }

    private void validatePasswordFormat(UserDto userDto, BindingResult bindingResult) {

        /**
         * 비밀번호 조건: 8자리 이상, 연속된 3자리 숫자 안됨, 특수기호 ~!@#$%^&*()_+ 중 하나 이상 포함할 것
         */
        String password = userDto.getPassword();
        log.info("비밀번호={}, 비밀번호 길이={}", password, password.length());
        if (password.length() < 8) {
            bindingResult.addError(new FieldError("userDto", "password", null, false, new String[]{"password.LengthGte8"}, null, null));
        }
        Pattern passwordSpecialPattern = Pattern.compile(".*[~!@#$%^&*()_+]+.*");
        if (!passwordSpecialPattern.matcher(password).matches()) {
            bindingResult.addError(new FieldError("userDto", "password", null, false, new String[]{"password.specialCode"}, null, null));
        }

        Pattern passwordContinuousPattern = Pattern.compile(".*(012|123|234|345|456|567|678|789|890).*");
        if (passwordContinuousPattern.matcher(password).matches()) {
            bindingResult.addError(new FieldError("userDto","password" ,null, false, new String[]{"password.notContinuous"}, null, null));
        }
    }

    private void validatePhoneFormat(UserDto userDto, BindingResult bindingResult) {
        /**
         * 전화번호는 국내 번호에 한정됨.
         * 전화번호 조건: (2~3자리)-(3-4자리)-(4자리)
         * 첫 2~3자리는 [지역 번호, 010, 011] 중 하나여야 함.
         * 지역번호
         *   서울: 02, 부산: 051, 대구: 053, 인천: 032, 광주: 062, 대전: 042, 울산 052,
         *   세종: 044, 경기: 031, 강원: 033, 충북: 043, 충남: 041, 전북: 063, 전남: 061,
         *   경북: 054, 경남: 055, 제주: 064
         */
        String phoneNumber = userDto.getPhoneNumber1() + "-" + userDto.getPhoneNumber2() + "-" + userDto.getPhoneNumber3();
        Pattern phoneNumberPattern = Pattern.compile("[0-9]{2,3}-[0-9]{3,4}-[0-9]{4}");
        if (!phoneNumberPattern.matcher(phoneNumber).matches()) {
            bindingResult.addError(new FieldError("userDto","phoneNumber2" ,null, false, new String[]{"phoneNumber.pattern"}, null, null));
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
            bindingResult.addError(new FieldError("userDto","phoneNumber1", userDto.getPhoneNumber1(), false, new String[]{"phoneNumber.notExists"}, null, null));
        }
    }
}
