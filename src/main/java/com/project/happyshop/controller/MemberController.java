package com.project.happyshop.controller;

import com.project.happyshop.domain.Address;
import com.project.happyshop.domain.SocialProvider;
import com.project.happyshop.domain.dto.UserDto;
import com.project.happyshop.domain.entity.Member;
import com.project.happyshop.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String register(UserDto userDto, RedirectAttributes redirectAttributes) {

        log.info(userDto.toString());
        Member member = Member.createMember(
                userDto.getEmail(),
                SocialProvider.LOCAL,
                userDto.getPassword(),
                userDto.getUsername(),
                userDto.getPhoneNumber1() + "-" + userDto.getPhoneNumber2() + "-" + userDto.getPhoneNumber3(),
                new Address(userDto.getJuso(), userDto.getJusoDetail(), userDto.getZipcode())
        );

        try {
            memberService.join(member);
        } catch (IllegalStateException e) {
            log.info(e.getMessage());
            return "redirect:/register";
        }

        redirectAttributes.addAttribute("status", true);
        redirectAttributes.addAttribute("username", member.getUsername());
        return "redirect:/login";
    }


}
