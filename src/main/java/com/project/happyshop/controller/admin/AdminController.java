package com.project.happyshop.controller.admin;

import com.project.happyshop.domain.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class AdminController {

    @GetMapping("/admin")
    public String home(@AuthenticationPrincipal Member member, Model model) {
        model.addAttribute("member", member);
        log.info("member={}", member.toString());
        return "admin/home";
    }
}
