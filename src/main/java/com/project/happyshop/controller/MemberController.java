package com.project.happyshop.controller;

import com.project.happyshop.domain.Address;
import com.project.happyshop.domain.SocialProvider;
import com.project.happyshop.domain.dto.SocialRegisterViewDto;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URLDecoder;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final RoleService roleService;
    private final MemberRoleService memberRoleService;

    @GetMapping("/register")
    public String register() {
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
    public String register(UserDto userDto) {
        log.info(userDto.toString());
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
}
