package com.project.happyshop.controller;

import com.project.happyshop.domain.Address;
import com.project.happyshop.domain.SocialProvider;
import com.project.happyshop.domain.dto.UserDto;
import com.project.happyshop.domain.entity.Member;
import com.project.happyshop.domain.entity.MemberRole;
import com.project.happyshop.domain.entity.Role;
import com.project.happyshop.service.MemberRoleService;
import com.project.happyshop.service.MemberService;
import com.project.happyshop.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping("/successRegister")
    public String successRegister() {
        return "/member/login/successRegister";
    }
}
